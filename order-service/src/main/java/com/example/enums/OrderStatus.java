package com.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of an order: PENDING, MATCHED, or CANCELED")
public enum OrderStatus {

    @Schema(description = "Order is created but not executed yet")
    PENDING,

    @Schema(description = "Order was successfully executed")
    MATCHED,

    @Schema(description = "Order was canceled by user")
    CANCELED
}
