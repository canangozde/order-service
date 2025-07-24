package com.example.dto;

import com.example.enums.OrderSide;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderDto(

        @NotNull(message = "Customer ID is required")
        @JsonProperty("customerId")
        @Schema(description = "ID of the customer placing the order", example = "1")
        Long customerId,

        @NotBlank(message = "Asset name is required")
        @JsonProperty("assetName")
        @Schema(description = "Name of the asset", example = "ING")
        String assetName,

        @NotNull(message = "Order side (BUY or SELL) is required")
        @JsonProperty("side")
        @Schema(description = "Side of the order (BUY or SELL)", implementation = OrderSide.class)
        OrderSide side,

        @NotNull(message = "Order size is required")
        @Positive(message = "Order size must be positive")
        @JsonProperty("size")
        @Schema(description = "Number of shares to buy/sell", example = "1")
        BigDecimal size,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        @JsonProperty("price")
        @Schema(description = "Price per share", example = "25.5")
        BigDecimal price

) {
}
