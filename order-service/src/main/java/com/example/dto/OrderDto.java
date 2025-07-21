package com.example.dto;

import com.example.enums.OrderSide;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @Schema(description = "ID of the customer placing the order", example = "1")
    private Long customerId;

    @Schema(description = "Name of the asset", example = "ING")
    private String assetName;

    @Schema(description = "Side of the order (BUY or SELL)", implementation = OrderSide.class)
    private OrderSide side;

    @Schema(description = "Number of shares to buy/sell", example = "100")
    private BigDecimal size;

    @Schema(description = "Price per share", example = "25.5")
    private BigDecimal price;
}
