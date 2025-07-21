
package com.example.service;

import com.example.dto.AuthDto;
import com.example.dto.RefreshTokenDto;
import com.example.model.Customer;
import com.example.model.Token;
import com.example.repository.CustomerRepository;
import com.example.repository.TokenRepository;
import com.example.response.JwtAuthenticationResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    private final long accessTokenExpiration = 1000L * 60 * 60; // 1 hour

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(customerRepository, tokenRepository, jwtTokenService, authenticationManager);
        Field field = authenticationService.getClass().getDeclaredField("accessTokenExpiration");
        field.setAccessible(true);
        field.set(authenticationService, accessTokenExpiration);
    }

    @Test
    void testLogin_Success() {
        AuthDto authDto = new AuthDto("user", "pass");
        Customer user = new Customer();
        user.setUsername("user");

        when(customerRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(tokenRepository.findAllByUserAndIsActive(user, true)).thenReturn(Optional.of(Collections.emptyList()));
        when(jwtTokenService.generateToken(user)).thenReturn("access-token");
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("refresh-token");

        JwtAuthenticationResponse response = authenticationService.login(authDto);

        assertNotNull(response);
        assertEquals("access-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertTrue(response.getExpiresAt().after(new Date()));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void testLogin_InvalidUsername() {
        when(customerRepository.findByUsername("user")).thenReturn(Optional.empty());

        AuthDto authDto = new AuthDto("user", "pass");
        assertThrows(IllegalArgumentException.class, () -> authenticationService.login(authDto));
    }

    @Test
    void testRefreshToken_ValidToken() {
        Customer user = new Customer();
        Token oldToken = new Token("old-access", "refresh-token", new Date(System.currentTimeMillis() + 100000), user, true);

        when(tokenRepository.findByRefreshToken("refresh-token")).thenReturn(Optional.of(oldToken));
        when(jwtTokenService.generateToken(user)).thenReturn("new-access-token");
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("new-refresh-token");

        JwtAuthenticationResponse response = authenticationService.refreshToken(new RefreshTokenDto("refresh-token"));

        assertNotNull(response);
        assertEquals("new-access-token", response.getToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        assertTrue(response.getExpiresAt().after(new Date()));
        verify(tokenRepository, times(2)).save(any(Token.class));
    }

    @Test
    void testRefreshToken_ExpiredOrInactive() {
        Customer user = new Customer();
        Token expiredToken = new Token("old-access", "refresh-token", new Date(System.currentTimeMillis() - 10000), user, false);

        when(tokenRepository.findByRefreshToken("refresh-token")).thenReturn(Optional.of(expiredToken));

        JwtAuthenticationResponse response = authenticationService.refreshToken(new RefreshTokenDto("refresh-token"));

        assertNull(response);
        verify(tokenRepository).delete(expiredToken);
    }

    @Test
    void testRefreshToken_NotFound() {
        when(tokenRepository.findByRefreshToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> authenticationService.refreshToken(new RefreshTokenDto("invalid-token")));
    }
}
