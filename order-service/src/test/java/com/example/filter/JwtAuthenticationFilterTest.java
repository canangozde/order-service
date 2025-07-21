
package com.example.filter;

import com.example.service.JwtTokenService;
import com.example.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserService userService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldDoNothingIfAuthorizationHeaderIsMissing() throws ServletException, IOException {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldDoNothingIfAuthorizationHeaderIsInvalid() throws ServletException, IOException {
        request.addHeader("Authorization", "InvalidHeader");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateIfJwtIsValid() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer valid.jwt.token");

        // Arrange
        UserDetailsService userDetailsServiceMock = mock(UserDetailsService.class);
        when(userService.userDetailService()).thenReturn(userDetailsServiceMock);
        when(jwtTokenService.extractUserName("valid.jwt.token")).thenReturn("user@example.com");
        when(userDetailsServiceMock.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtTokenService.isTokenValid("valid.jwt.token", userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(new java.util.ArrayList<>());

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtTokenService).extractUserName("valid.jwt.token");
        verify(jwtTokenService).isTokenValid("valid.jwt.token", userDetails);
        verify(filterChain).doFilter(request, response);
    }
}
