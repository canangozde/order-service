package com.example.service;

import com.example.dto.OrderDto;
import com.example.enums.OrderSide;
import com.example.enums.OrderStatus;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Asset;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.repository.AssetRepository;
import com.example.repository.CustomerRepository;
import com.example.repository.OrderRepository;
import com.example.repository.specification.OrderSpecification;
import com.example.response.OrderResponse;
import com.example.util.AuthUtil;
import com.example.util.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.constants.Constants.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final AuthUtil authUtil;

    public List<OrderResponse> getOrders(Long customerId,
                                         LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         OrderStatus status,
                                         OrderSide side,
                                         Long assetId,
                                         BigDecimal minPrice,
                                         BigDecimal maxPrice,
                                         BigDecimal minSize,
                                         BigDecimal maxSize) {

        if(customerId != null && !authUtil.isAdminOrAccessingOwnData(customerId)) {
            throw new AccessDeniedException("You can only view your own orders");
        }

        if(!authUtil.isAdmin()){
            customerId = authUtil.getCustomerId();
        }

        Specification<Order> spec = Specification
                .where(OrderSpecification.hasCustomerId(customerId))
                .and(OrderSpecification.hasCreateDateAfter(startDate))
                .and(OrderSpecification.hasCreateDateBefore(endDate))
                .and(OrderSpecification.hasStatus(status))
                .and(OrderSpecification.hasOrderSide(side))
                .and(OrderSpecification.hasAssetId(assetId))
                .and(OrderSpecification.hasPriceGreaterThanOrEqual(minPrice))
                .and(OrderSpecification.hasPriceLessThanOrEqual(maxPrice))
                .and(OrderSpecification.hasSizeGreaterThanOrEqual(minSize))
                .and(OrderSpecification.hasSizeLessThanOrEqual(maxSize));

        List<Order> orders = orderRepository.findAll(spec);

        if (orders.isEmpty()) {
            throw new EntityNotFoundException("No orders found for given filters");
        }

        return orders.stream().map(orderMapper::fromOrder).collect(Collectors.toList());
    }


    @Transactional
    public OrderResponse createOrder(@Validated OrderDto orderDto) {

        Long customerId = orderDto.customerId();
        String assetName = orderDto.assetName();

        if (!authUtil.isAdminOrAccessingOwnData(customerId)) {
            throw new AccessDeniedException("You can only create orders for yourself");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(CUSTOMER_NOT_FOUND));

        Asset asset = assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .orElseThrow(() -> new EntityNotFoundException(ASSET_NOT_FOUND));

        if (OrderSide.BUY == orderDto.side()) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, TRY)
                    .orElseThrow(() -> new EntityNotFoundException(TRY_ASSET_NOT_FOUND));

            BigDecimal totalCost = orderDto.size().multiply(orderDto.price());

            if (tryAsset.getUsableSize().compareTo(totalCost) < 0) {
                throw new IllegalStateException("Insufficient TRY usable balance");
            }

            tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(totalCost));
            assetRepository.save(tryAsset);

        } else if (OrderSide.SELL == orderDto.side()) {
            if (asset.getUsableSize().compareTo(orderDto.size()) < 0) {
                throw new IllegalStateException("Insufficient asset usable size to sell");
            }

            asset.setUsableSize(asset.getUsableSize().subtract(orderDto.size()));
            asset.setSize(asset.getSize().subtract(orderDto.size()));
            assetRepository.save(asset);
        } else {
            throw new IllegalArgumentException("Invalid order side: " + orderDto.side());
        }

        Order order = orderMapper.toOrder(orderDto, asset, customer);
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());
        return orderMapper.fromOrder(orderRepository.save(order));
    }

    @Transactional
    public void deleteOrder(@NotNull Long orderId) {
        if (!authUtil.isAdminOrAccessingOwnData(orderId)) {
            throw new AccessDeniedException("You can only cancel your own orders");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));

        if (OrderStatus.PENDING != order.getStatus()) {
            throw new IllegalStateException("Only PENDING orders can be cancelled");
        }

        Asset asset = order.getAsset();
        Long customerId = order.getCustomer().getId();

        if (OrderSide.BUY == order.getOrderSide()) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, TRY)
                    .orElseThrow(() -> new EntityNotFoundException(TRY_ASSET_NOT_FOUND));

            BigDecimal refund = order.getSize().multiply(order.getPrice());
            tryAsset.setUsableSize(tryAsset.getUsableSize().add(refund));
            assetRepository.save(tryAsset);

        } else if (OrderSide.SELL == order.getOrderSide()) {
            asset.setSize(asset.getSize().add(order.getSize()));
            asset.setUsableSize(asset.getUsableSize().add(order.getSize()));
            assetRepository.save(asset);
        } else {
            throw new IllegalArgumentException("Invalid order side: " + order.getOrderSide());
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Transactional
    public void matchOrder(@NotNull Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));

        if (OrderStatus.PENDING != order.getStatus()) {
            throw new IllegalStateException("Only pending orders can be matched");
        }

        Long customerId = order.getCustomer().getId();
        BigDecimal totalCost = order.getSize().multiply(order.getPrice());

        Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, TRY)
                .orElseThrow(() -> new ResourceNotFoundException(TRY_ASSET_NOT_FOUND));

        Asset tradedAsset = assetRepository.findByCustomerIdAndAssetName(customerId, order.getAsset().getAssetName())
                .orElseThrow(() -> new ResourceNotFoundException(TRADED_ASSET_NOT_FOUND));

        if (OrderSide.BUY == order.getOrderSide()) {
            if (tryAsset.getUsableSize().compareTo(totalCost) < 0) {
                throw new IllegalStateException("Not enough TRY balance to match the BUY order");
            }

            tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(totalCost));
            tradedAsset.setSize(tradedAsset.getSize().add(order.getSize()));
            tradedAsset.setUsableSize(tradedAsset.getUsableSize().add(order.getSize()));

        } else if (OrderSide.SELL == order.getOrderSide()) {
            if (tradedAsset.getUsableSize().compareTo(order.getSize()) < 0) {
                throw new IllegalStateException("Not enough asset balance to match the SELL order");
            }

            tradedAsset.setSize(tradedAsset.getSize().subtract(order.getSize()));
            tradedAsset.setUsableSize(tradedAsset.getUsableSize().subtract(order.getSize()));

            tryAsset.setUsableSize(tryAsset.getUsableSize().add(totalCost));
        } else {
            throw new IllegalArgumentException("Invalid order side: " + order.getOrderSide());
        }

        order.setStatus(OrderStatus.MATCHED);
        orderRepository.save(order);
        assetRepository.saveAll(List.of(tradedAsset, tryAsset));
    }
}
