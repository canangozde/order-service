package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthDto(
        @Schema(description = "Username of the customer", example = "user")
        @NotBlank(message = "Username is required")
        @JsonProperty("username")
        String username,

        @Schema(description = "Password of the customer", example = "user123")
        @NotBlank(message = "Password is required")
        @JsonProperty("password")
        String password
) {
}

