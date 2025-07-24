package com.example.util;

import com.example.dto.OrderDto;
import com.example.model.Asset;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "asset", source = "asset")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "orderSide", source = "orderDto.side")
    @Mapping(target = "size", source = "orderDto.size")
    @Mapping(target = "price", source = "orderDto.price")
    Order toOrderInternal(OrderDto orderDto, Asset asset, Customer customer);

    default Order toOrder(OrderDto orderDto, Asset asset, Customer customer) {
        if (orderDto == null || asset == null || customer == null) {
            return null;
        }
        return toOrderInternal(orderDto, asset, customer);
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "assetName", source = "asset.assetName")
    @Mapping(target = "side", source = "orderSide")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createDate", source = "createDate")
    OrderResponse fromOrder(Order order);
}

