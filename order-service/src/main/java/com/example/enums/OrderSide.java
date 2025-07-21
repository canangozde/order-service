package com.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Side of the order: BUY or SELL")
public enum OrderSide {
    @Schema(description = "Customer wants to buy shares")
    BUY,

    @Schema(description = "Customer wants to sell shares")
    SELL
}
