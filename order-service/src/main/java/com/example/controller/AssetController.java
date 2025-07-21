package com.example.controller;

import com.example.response.AssetResponse;
import com.example.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/assets", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssetController {

    private final AssetService assetService;

    @Operation(summary = "List assets for a customer",
            description = "Lists all assets (including TRY) held by a customer.",
            security = @SecurityRequirement(name = "jwtAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @GetMapping("/customerId/{customerId}")
    public ResponseEntity<List<AssetResponse>> listAssets(@PathVariable Long customerId) {

        return ResponseEntity.ok(assetService.listAssets(customerId));
    }
}
