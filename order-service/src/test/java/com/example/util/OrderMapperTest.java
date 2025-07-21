
package com.example.util;

import com.example.dto.OrderDto;
import com.example.enums.OrderSide;
import com.example.model.Asset;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    void shouldMapOrderDtoToOrder() {
        OrderDto dto = new OrderDto();
        dto.setCustomerId(1L);
        dto.setSide(OrderSide.BUY);
        dto.setSize(BigDecimal.valueOf(10));
        dto.setPrice(BigDecimal.valueOf(100));

        Asset asset = new Asset();
        asset.setAssetName("BTC");

        Customer customer = new Customer();
        customer.setId(1L);

        Order order = orderMapper.toOrder(dto, asset, customer);

        assertNotNull(order);
        assertEquals(1L, order.getCustomer().getId());
        assertEquals(OrderSide.BUY, order.getOrderSide());
        assertEquals(BigDecimal.valueOf(10), order.getSize());
        assertEquals(BigDecimal.valueOf(100), order.getPrice());
        assertEquals(asset, order.getAsset());
    }

    @Test
    void shouldReturnNullWhenOrderDtoIsNull() {
        Order order = orderMapper.toOrder(null, new Asset(), new Customer());
        assertNull(order);
    }

    @Test
    void shouldMapOrderToOrderResponse() {
        Asset asset = new Asset();
        asset.setAssetName("ETH");

        Customer customer = new Customer();
        customer.setId(1L);

        Order order = Order.builder()
                .id(1L)
                .customer(customer)
                .orderSide(OrderSide.SELL)
                .size(BigDecimal.valueOf(5))
                .price(BigDecimal.valueOf(2000))
                .createDate(LocalDateTime.now())
                .asset(asset)
                .build();

        OrderResponse response = orderMapper.fromOrder(order);

        assertNotNull(response);
        assertEquals(order.getId(), response.getId());
        assertEquals(1L, response.getCustomerId());
        assertEquals("ETH", response.getAssetName());
        assertEquals(OrderSide.SELL, response.getSide());
        assertEquals(BigDecimal.valueOf(5), response.getSize());
        assertEquals(BigDecimal.valueOf(2000), response.getPrice());
        assertEquals(order.getCreateDate(), response.getCreateDate());
    }
}
