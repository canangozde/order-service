package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;
    String refreshToken;
    Date expiresAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    Customer user;
    Boolean isActive;

    public Token(String jwt, String refreshToken, Date expiresAt, Customer user, Boolean isActive) {
        this.token = jwt;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.user = user;
        this.isActive = isActive;
    }
}
