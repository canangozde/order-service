package com.example.controller;

import com.example.response.AssetResponse;
import com.example.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.example.constants.Constants.*;

@SecurityRequirement(name = BEAERER_AUTH)
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/assets", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssetController {

    private final AssetService assetService;

    @Operation(summary = ASSETS_SUMMARY,
            description = ASSETS_DESC,
            security = @SecurityRequirement(name = JWT_AUTH))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_CODE,
                    description = ASSETS_OK_DESC,
                    content = @Content(
                            mediaType = APPLICATION_JSON,
                            array = @ArraySchema(schema = @Schema(implementation = AssetResponse.class)),
                            examples = @ExampleObject(value = ASSETS_OK_EXAMPLE)
                    )
            ),
            @ApiResponse(
                    responseCode = ACCESS_DENIED_CODE,
                    description = ASSETS_ACCESS_DENIED_DESC,
                    content = @Content(
                            mediaType = APPLICATION_JSON,
                            examples = @ExampleObject(value = ASSETS_ACCESS_DENIED_EXAMPLE)
                    )
            ),
            @ApiResponse(
                    responseCode = NOT_FOUND_CODE,
                    description = ASSETS_NOT_FOUND_DESC,
                    content = @Content(
                            mediaType = APPLICATION_JSON,
                            examples = @ExampleObject(value = ASSETS_NOT_FOUND_EXAMPLE)
                    )
            ),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_CODE,
                    description = ASSETS_INTERNAL_ERROR_DESC,
                    content = @Content(
                            mediaType = APPLICATION_JSON,
                            examples = @ExampleObject(value = ASSETS_INTERNAL_ERROR_EXAMPLE)
                    )
            )
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<AssetResponse>> listAssets(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String assetName,
            @RequestParam(required = false) BigDecimal minSize,
            @RequestParam(required = false) BigDecimal maxSize,
            @RequestParam(required = false) BigDecimal minUsableSize,
            @RequestParam(required = false) BigDecimal maxUsableSize
    ) {
        List<AssetResponse> assets = assetService.listAssets(customerId, assetName, minSize, maxSize, minUsableSize, maxUsableSize);
        return ResponseEntity.ok(assets);
    }

}
