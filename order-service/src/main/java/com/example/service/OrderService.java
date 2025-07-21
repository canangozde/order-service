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
import com.example.response.OrderResponse;
import com.example.util.AuthUtil;
import com.example.util.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final AuthUtil authUtil;

    public List<OrderResponse> getOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {

        if (!authUtil.isAdminOrAccessingOwnData(customerId)) {
            throw new AccessDeniedException("You can only view your own orders");
        }

        return orderRepository.findAllByCustomerIdAndCreateDateBetween(customerId, startDate, endDate)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new EntityNotFoundException("No orders found for customerId: " + customerId))
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse createOrder(OrderDto orderDto) {

        Long customerId = orderDto.getCustomerId();
        String assetName = orderDto.getAssetName();

        if (!authUtil.isAdminOrAccessingOwnData(customerId)) {
            throw new AccessDeniedException("You can only create orders for yourself");
        }

        Asset asset = assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (orderDto.getSide() == OrderSide.BUY) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")
                    .orElseThrow(() -> new EntityNotFoundException("TRY asset not found"));

            BigDecimal totalCost = orderDto.getSize().multiply(orderDto.getPrice());

            if (tryAsset.getUsableSize().compareTo(totalCost) < 0) {
                throw new IllegalStateException("Insufficient TRY usable balance");
            }

            tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(totalCost));
            assetRepository.save(tryAsset);

        } else if (orderDto.getSide() == OrderSide.SELL) {
            if (asset.getUsableSize().compareTo(orderDto.getSize()) < 0) {
                throw new IllegalStateException("Insufficient asset usable size to sell");
            }

            asset.setUsableSize(asset.getUsableSize().subtract(orderDto.getSize()));
            asset.setSize(asset.getSize().subtract(orderDto.getSize()));
            assetRepository.save(asset);
        }

        Order order = orderMapper.toOrder(orderDto, asset, customer);
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());
        return orderMapper.fromOrder(orderRepository.save(order));
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        if (!authUtil.isAdminOrAccessingOwnData(orderId)) {
            throw new AccessDeniedException("You can only cancel your own orders");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be cancelled");
        }

        Asset asset = order.getAsset();
        Long customerId = order.getCustomer().getId();

        if (order.getOrderSide() == OrderSide.BUY) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")
                    .orElseThrow(() -> new EntityNotFoundException("TRY asset not found"));

            BigDecimal refund = order.getSize().multiply(order.getPrice());
            tryAsset.setUsableSize(tryAsset.getUsableSize().add(refund));
            assetRepository.save(tryAsset);

        } else if (order.getOrderSide() == OrderSide.SELL) {
            asset.setSize(asset.getSize().add(order.getSize()));
            asset.setUsableSize(asset.getUsableSize().add(order.getSize()));
            assetRepository.save(asset);
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Transactional
    public void matchOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be matched");
        }

        Long customerId = order.getCustomer().getId();
        BigDecimal totalCost = order.getSize().multiply(order.getPrice());

        Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")
                .orElseThrow(() -> new ResourceNotFoundException("TRY asset not found"));

        Asset tradedAsset = assetRepository.findByCustomerIdAndAssetName(customerId, order.getAsset().getAssetName())
                .orElseThrow(() -> new ResourceNotFoundException("Traded asset not found"));

        if (order.getOrderSide() == OrderSide.BUY) {
            if (tryAsset.getUsableSize().compareTo(totalCost) < 0) {
                throw new IllegalStateException("Not enough TRY balance to match the BUY order");
            }

            tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(totalCost));
            tradedAsset.setSize(tradedAsset.getSize().add(order.getSize()));
            tradedAsset.setUsableSize(tradedAsset.getUsableSize().add(order.getSize()));

        } else if (order.getOrderSide() == OrderSide.SELL) {
            if (tradedAsset.getUsableSize().compareTo(order.getSize()) < 0) {
                throw new IllegalStateException("Not enough asset balance to match the SELL order");
            }

            tradedAsset.setSize(tradedAsset.getSize().subtract(order.getSize()));
            tradedAsset.setUsableSize(tradedAsset.getUsableSize().subtract(order.getSize()));

            tryAsset.setUsableSize(tryAsset.getUsableSize().add(totalCost));
        }

        order.setStatus(OrderStatus.MATCHED);
        orderRepository.save(order);
        assetRepository.saveAll(List.of(tradedAsset, tryAsset));
    }
}
