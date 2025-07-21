package com.example.util;

import com.example.dto.OrderDto;
import com.example.model.Asset;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrder(OrderDto orderDto, Asset asset, Customer customer) {
        if (orderDto == null || asset == null || customer == null) {
            return null;
        }
        return Order.builder()
                .customer(customer)
                .asset(asset)
                .orderSide(orderDto.getSide())
                .size(orderDto.getSize())
                .price(orderDto.getPrice())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .assetName(order.getAsset().getAssetName())
                .side(order.getOrderSide())
                .size(order.getSize())
                .price(order.getPrice())
                .status(order.getStatus())
                .createDate(order.getCreateDate())
                .build();
    }
}
