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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.constants.Constants.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(
            summary = AUTH_SUMMARY,
            description = AUTH_DESC
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_CODE,
                    description = AUTH_OK_DESC,
                    content = @Content(mediaType = APPLICATION_JSON,
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))
            ),
            @ApiResponse(
                    responseCode = UNAUTHORIZED_CODE,
                    description = AUTH_UNAUTHORIZED_DESC,
                    content = @Content(mediaType = APPLICATION_JSON,
                            schema = @Schema(
                                    example = AUTH_UNAUTHORIZED_EXAMPLE
                            ))
            ),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_CODE,
                    description = AUTH_INTERNAL_ERROR_DESC,
                    content = @Content(mediaType = APPLICATION_JSON,
                            schema = @Schema(
                                    example = AUT_INTERNAL_ERROR_EXAMPLE
                            ))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody AuthDto request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh/access")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenDto request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

}