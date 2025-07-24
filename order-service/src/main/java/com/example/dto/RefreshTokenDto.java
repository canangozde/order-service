package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto(

        @NotBlank(message = "Refresh token is required")
        @JsonProperty("refreshToken")
        String refreshToken

) {
}
