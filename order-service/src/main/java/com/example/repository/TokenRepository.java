package com.example.repository;

import com.example.model.Customer;
import com.example.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    Optional<List<Token>> findAllByUserAndIsActive(Customer user, Boolean isActive);

    Optional<Token> findByRefreshToken(String refreshToken);
}
