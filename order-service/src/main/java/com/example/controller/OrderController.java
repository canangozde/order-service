package com.example.controller;

import com.example.dto.OrderDto;
import com.example.response.OrderResponse;
import com.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new stock order for a customer",
            description = "Creates a PENDING order. BUY requires sufficient TRY balance; SELL requires asset availability.",
            security = @SecurityRequirement(name = "jwtAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders(@RequestParam(value = "customerId", required = false) Long customerId,
                                                          @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                          @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return ResponseEntity.ok(orderService.getOrders(customerId, startDate, endDate));
    }

    @Operation(summary = "Get orders for a customer in a date range",
            description = "Only the customer's own orders can be viewed unless user is admin.",
            security = @SecurityRequirement(name = "jwtAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderDto orderDto) {

        OrderResponse orderResponse = orderService.createOrder(orderDto);
        return ResponseEntity.created(
                URI.create("/api/v1/orders/"
                        + orderResponse.getId())
        ).body(orderResponse);
    }

    @Operation(summary = "Cancel a pending order",
            description = "Only PENDING orders can be cancelled. Order status is changed to CANCELED, balances updated.",
            security = @SecurityRequirement(name = "jwtAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@PathVariable Long id) {

        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/match")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Match a pending order", description = "Only ADMIN users can match orders")
    public ResponseEntity<String> matchOrder(@PathVariable Long orderId) {

        orderService.matchOrder(orderId);
        return ResponseEntity.ok("Order matched successfully.");
    }
}
