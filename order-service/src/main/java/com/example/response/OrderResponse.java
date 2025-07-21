package com.example.response;

import com.example.enums.OrderSide;
import com.example.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;

    private Long customerId;

    private String assetName;

    @Schema(description = "BUY or SELL", implementation = OrderSide.class)
    private OrderSide side;

    private BigDecimal size;

    private BigDecimal price;

    @Schema(description = "Order status", implementation = OrderStatus.class)
    private OrderStatus status;

    private LocalDateTime createDate;
}
