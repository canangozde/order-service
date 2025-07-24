package com.example.util;

import com.example.dto.OrderDto;
import com.example.model.Asset;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.response.OrderResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-24T15:28:41+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toOrderInternal(OrderDto orderDto, Asset asset, Customer customer) {
        if ( orderDto == null && asset == null && customer == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        if ( orderDto != null ) {
            order.orderSide( orderDto.side() );
            order.size( orderDto.size() );
            order.price( orderDto.price() );
        }
        order.asset( asset );
        order.customer( customer );

        return order.build();
    }

    @Override
    public OrderResponse fromOrder(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.id( order.getId() );
        orderResponse.customerId( orderCustomerId( order ) );
        orderResponse.assetName( orderAssetAssetName( order ) );
        orderResponse.side( order.getOrderSide() );
        orderResponse.size( order.getSize() );
        orderResponse.price( order.getPrice() );
        orderResponse.status( order.getStatus() );
        orderResponse.createDate( order.getCreateDate() );

        return orderResponse.build();
    }

    private Long orderCustomerId(Order order) {
        Customer customer = order.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String orderAssetAssetName(Order order) {
        Asset asset = order.getAsset();
        if ( asset == null ) {
            return null;
        }
        return asset.getAssetName();
    }
}
