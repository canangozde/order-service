
package com.example.service;

import com.example.model.Token;
import com.example.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserDetails userDetails;

    private String secretKey;
    private String refreshKey;

    @BeforeEach
    void setup() throws Exception {
        secretKey = Base64.getEncoder().encodeToString("mysecretkeymysecretkeymysecretkey12".getBytes()); // 32-byte key
        refreshKey = Base64.getEncoder().encodeToString("refreshkeyrefreshkeyrefreshkey1234".getBytes()); // 32-byte key

        Field jwtKeyField = JwtTokenService.class.getDeclaredField("jwtSigningKey");
        jwtKeyField.setAccessible(true);
        jwtKeyField.set(jwtTokenService, secretKey);

        Field refreshKeyField = JwtTokenService.class.getDeclaredField("jwtRefreshSigningKey");
        refreshKeyField.setAccessible(true);
        refreshKeyField.set(jwtTokenService, refreshKey);

        Field accessExpField = JwtTokenService.class.getDeclaredField("accessTokenExpiration");
        accessExpField.setAccessible(true);
        accessExpField.set(jwtTokenService, 3600000L);

        Field refreshExpField = JwtTokenService.class.getDeclaredField("refreshTokenExpiration");
        refreshExpField.setAccessible(true);
        refreshExpField.set(jwtTokenService, 7200000L);

        when(userDetails.getUsername()).thenReturn("dummyUser");
    }

    @Test
    void shouldGenerateValidTokenAndValidateSuccessfully() {
        String token = jwtTokenService.generateToken(userDetails);

        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setIsActive(true);

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        boolean valid = jwtTokenService.isTokenValid(token, userDetails);
        assertTrue(valid);
    }
    
    @Test
    void shouldExtractUsernameFromValidToken() {
        String token = jwtTokenService.generateToken(userDetails);
        String username = jwtTokenService.extractUserName(token);
        assertEquals("dummyUser", username);
    }

    @Test
    void shouldExtractExpirationCorrectly() {
        String token = jwtTokenService.generateToken(userDetails);
        Date expiration = jwtTokenService.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void shouldThrowExceptionIfTokenNotFoundInDb() {
        String token = jwtTokenService.generateToken(userDetails);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> jwtTokenService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldGenerateRefreshTokenWithPrefix() {
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        assertTrue(refreshToken.startsWith("REFRESH_"));
    }

    private String createExpiredToken(String username) {
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 3600000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(String keyStr) {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
