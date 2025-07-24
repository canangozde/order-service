package com.example.controller;

import com.example.dto.OrderDto;
import com.example.enums.OrderSide;
import com.example.enums.OrderStatus;
import com.example.response.OrderResponse;
import com.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.constants.Constants.*;

@SecurityRequirement(name = BEAERER_AUTH)
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = GET_ORDERS_SUMMARY,
            description = GET_ORDERS_DESC,
            security = @SecurityRequirement(name = JWT_AUTH),
            parameters = {
                    @Parameter(
                            name = CUSTOMER_ID_NAME,
                            description = CUSTOMER_ID_DESC,
                            example = CUSTOMER_ID_EXAMPLE),
                    @Parameter(
                            name = START_DATE_NAME,
                            description = START_DATE_DESC,
                            example = START_DATE_EXAMPLE),
                    @Parameter(
                            name = END_DATE_NAME,
                            description = END_DATE_DESC,
                            example = END_DATE_EXAMPLE),
                    @Parameter(
                            name = STATUS_NAME,
                            description = STATUS_DESC,
                            example = STATUS_EXAMPLE),
                    @Parameter(
                            name = SIDE_NAME,
                            description = SIDE_DESC,
                            example = SIDE_EXAMPLE),
                    @Parameter(
                            name = ASSET_ID_NAME,
                            description = ASSET_ID_DESC,
                            example = ASSET_ID_EXAMPLE),
                    @Parameter(
                            name = MIN_PRICE_NAME,
                            description = MIN_PRICE_DESC,
                            example = MIN_PRICE_EXAMPLE),
                    @Parameter(
                            name = MAX_PRICE_NAME,
                            description = MAX_PRICE_DESC,
                            example = MAX_PRICE_EXAMPLE),
                    @Parameter(
                            name = MIN_SIZE_NAME,
                            description = MIN_SIZE_DESC,
                            example = MIN_SIZE_EXAMPLE),
                    @Parameter(
                            name = MAX_SIZE_NAME,
                            description = MAX_SIZE_DESC,
                            example = MAX_SIZE_EXAMPLE)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_CODE,
                    description = GET_ORDERS_OK_DESC,
                    content = @Content(
                            mediaType = APPLICATION_JSON,
                            array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class)),
                            examples = @ExampleObject(value = GET_ORDERS_OK_EXAMPLE)
                    )
            ),
            @ApiResponse(
                    responseCode = ACCESS_DENIED_CODE,
                    description = GET_ORDERS_ACCESS_DENIED_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = GET_ORDERS_ACCESS_DENIED_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_CODE,
                    description = GET_ORDERS_INTERNAL_ERROR_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = GET_ORDERS_INTERNAL_ERROR_EXAMPLE))
            )
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders(
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "side", required = false) OrderSide side,
            @RequestParam(value = "assetId", required = false) Long assetId,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "minSize", required = false) BigDecimal minSize,
            @RequestParam(value = "maxSize", required = false) BigDecimal maxSize
    ) {
        return ResponseEntity.ok(
                orderService.getOrders(customerId, startDate, endDate, status, side, assetId, minPrice, maxPrice, minSize, maxSize)
        );
    }


    @Operation(
            summary = CREATE_ORDER_SUMMARY,
            description = CREATE_ORDER_DESC,
            security = @SecurityRequirement(name = JWT_AUTH)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = CREATED_CODE,
                    description = ORDER_CREATED_DESC,
                    content = @Content(
                            mediaType = APPLICATION_JSON,
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(value = ORDER_CREATED_EXAMPLE)
                    )
            ),
            @ApiResponse(
                    responseCode = BAD_REQUEST_CODE,
                    description = CREATE_ORDER_BAD_REQUEST_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = CREATE_ORDER_BAD_REQUEST_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = ACCESS_DENIED_CODE,
                    description = ORDER_CREATE_ACCESS_DENIED_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = ORDER_CREATE_ACCESS_DENIED_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_CODE,
                    description = CREATE_ORDER_INTERNAL_ERROR_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = CREATE_ORDER_INTERNAL_ERROR_EXAMPLE))
            )
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderDto orderDto) {

        OrderResponse orderResponse = orderService.createOrder(orderDto);
        return ResponseEntity.created(
                URI.create("/api/v1/orders/"
                        + orderResponse.getId())
        ).body(orderResponse);

    }

    @Operation(
            summary = DELETE_ORDER_SUMMARY,
            description = DELETE_ORDER_DESC,
            security = @SecurityRequirement(name = JWT_AUTH)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_CODE,
                    description = DELETE_ORDER_OK_DESC
            ),
            @ApiResponse(
                    responseCode = NOT_FOUND_CODE,
                    description = ORDER_NOT_FOUND_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = ORDER_NOT_FOUND_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = ACCESS_DENIED_CODE,
                    description = DELETE_ORDER_ACCESS_DENIED_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = DELETE_ORDER_ACCESS_DENIED_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_CODE,
                    description = DELETE_ORDER_INTERNAL_ERROR_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = DELETE_ORDER_INTERNAL_ERROR_EXAMPLE))
            )
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@Valid @PathVariable Long id) {

        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();

    }

    @Operation(
            summary = MATCH_ORDER_SUMMARY,
            description = MATCH_ORDER_DESC
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = OK_CODE,
                    description = ORDER_MATCHED_DESC),
            @ApiResponse(
                    responseCode = ACCESS_DENIED_CODE,
                    description = MATCH_ORDER_ACCESS_DENIED_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = MATCH_ORDER_ACCESS_DENIED_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = NOT_FOUND_CODE,
                    description = MATCH_ORDER_NOT_FOUND_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = MATCH_ORDER_NOT_FOUND_EXAMPLE))
            ),
            @ApiResponse(
                    responseCode = INTERNAL_SERVER_ERROR_CODE,
                    description = MATCH_ORDER_INTERNAL_ERROR_DESC,
                    content = @Content(mediaType = APPLICATION_JSON, examples = @ExampleObject(value = MATCH_ORDER_INTERNAL_ERROR_EXAMPLE))
            )
    })
    @PostMapping("/{orderId}/match")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> matchOrder(@Valid @PathVariable Long orderId) {

        orderService.matchOrder(orderId);
        return ResponseEntity.ok("Order matched successfully.");

    }
}
