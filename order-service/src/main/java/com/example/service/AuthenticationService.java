package com.example.service;

import com.example.dto.AuthDto;
import com.example.dto.RefreshTokenDto;
import com.example.model.Token;
import com.example.repository.CustomerRepository;
import com.example.repository.TokenRepository;
import com.example.response.JwtAuthenticationResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    @Value("${token.access.token.expiration}")
    private long accessTokenExpiration;

    @Transactional
    public JwtAuthenticationResponse login(AuthDto request) {

        var user = customerRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password(), user.getAuthorities()));

        List<Token> activeTokens = tokenRepository.findAllByUserAndIsActive(user, true).orElse(Collections.emptyList());
        if (!activeTokens.isEmpty()) {
            for (Token activeToken : activeTokens) {
                activeToken.setIsActive(false);
            }
            tokenRepository.saveAll(activeTokens);
        }

        var generateToken = jwtTokenService.generateToken(user);
        var refreshToken = jwtTokenService.generateRefreshToken(user);
        var expiresAt = new Date(System.currentTimeMillis() + accessTokenExpiration);

        tokenRepository.save(new Token(generateToken, refreshToken, expiresAt, user, true));

        return JwtAuthenticationResponse.builder().token(generateToken).refreshToken(refreshToken).expiresAt(expiresAt).build();
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenDto request) {

        Token token = tokenRepository.findByRefreshToken(request.refreshToken()).orElseThrow(() -> new EntityNotFoundException(request.refreshToken()));

        if (token.getExpiresAt().after(new Date()) && token.getIsActive()) {
            var user = token.getUser();
            var newAccessToken = jwtTokenService.generateToken(user);
            var newRefreshToken = jwtTokenService.generateRefreshToken(user);
            var expiresAt = new Date(System.currentTimeMillis() + accessTokenExpiration);
            token.setIsActive(false);
            tokenRepository.save(token);
            tokenRepository.save(new Token(newAccessToken, newRefreshToken, expiresAt, user, true));
            return new JwtAuthenticationResponse(newAccessToken, newRefreshToken, expiresAt);

        } else {
            tokenRepository.delete(token);
            return null;
        }
    }
}
