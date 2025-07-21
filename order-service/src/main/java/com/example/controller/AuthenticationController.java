package com.example.controller;

import com.example.dto.AuthDto;
import com.example.dto.RefreshTokenDto;
import com.example.response.JwtAuthenticationResponse;
import com.example.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Login and retrieve JWT token",
            description = "Authenticates the user and returns a JWT token if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid username or password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                                {
                                                  "error": "Unauthorized",
                                                  "message": "Invalid credentials",
                                                  "status": 401,
                                                  "timestamp": "2025-07-12T14:30:00.000"
                                                }
                                            """
                            ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Authentication processing failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                                {
                                                  "error": "Internal Server Error",
                                                  "message": "Unexpected authentication failure",
                                                  "status": 500,
                                                  "timestamp": "2025-07-12T14:30:00.000"
                                                }
                                            """
                            ))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody AuthDto request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh/access")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenDto request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

}