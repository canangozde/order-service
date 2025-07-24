package com.example.response;

import com.example.enums.OrderSide;
import com.example.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object representing an order placed by a customer")
public class OrderResponse {

    @Schema(description = "Unique identifier of the order", example = "1001")
    private Long id;

    @Schema(description = "ID of the customer who placed the order", example = "42")
    private Long customerId;

    @Schema(description = "Name of the asset involved in the order", example = "ING")
    private String assetName;

    @Schema(description = "Side of the order, BUY or SELL", implementation = OrderSide.class, example = "SELL")
    private OrderSide side;

    @Schema(description = "Total amount of the asset to buy or sell", example = "5.0")
    private BigDecimal size;

    @Schema(description = "Price per unit of the asset", example = "2000.00")
    private BigDecimal price;

    @Schema(description = "Current status of the order", implementation = OrderStatus.class, example = "PENDING")
    private OrderStatus status;

    @Schema(description = "Date and time when the order was created", example = "2025-07-24T14:30:00")
    private LocalDateTime createDate;
}
