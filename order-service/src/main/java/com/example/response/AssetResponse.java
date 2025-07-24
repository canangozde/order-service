package com.example.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents a customer's asset, such as stocks or cryptocurrencies, including its size and usability.")
public class AssetResponse {

    @Schema(description = "Unique identifier of the asset record", example = "101")
    private Long id;

    @Schema(description = "Name of the asset (e.g., stock symbol, crypto name)", example = "BTC")
    private String assetName;

    @Schema(description = "Total size of the asset held", example = "3.25")
    private BigDecimal size;

    @Schema(description = "Portion of the asset that is usable", example = "2.00")
    private BigDecimal usableSize;

    @Schema(description = "ID of the customer who owns the asset", example = "42")
    private Long customerId;
}
