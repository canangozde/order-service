package com.example.repository.specification;

import com.example.enums.OrderSide;
import com.example.enums.OrderStatus;
import com.example.model.Order;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSpecification {

    public static Specification<Order> hasCustomerId(Long customerId) {
        return (root, query, builder) ->
                customerId == null ? null : builder.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<Order> hasAssetId(Long assetId) {
        return (root, query, builder) ->
                assetId == null ? null : builder.equal(root.get("asset").get("id"), assetId);
    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, builder) ->
                status == null ? null : builder.equal(root.get("status"), status);
    }

    public static Specification<Order> hasOrderSide(OrderSide side) {
        return (root, query, builder) ->
                side == null ? null : builder.equal(root.get("orderSide"), side);
    }

    public static Specification<Order> hasCreateDateAfter(LocalDateTime startDate) {
        return (root, query, builder) ->
                startDate == null ? null : builder.greaterThanOrEqualTo(root.get("createDate"), startDate);
    }

    public static Specification<Order> hasCreateDateBefore(LocalDateTime endDate) {
        return (root, query, builder) ->
                endDate == null ? null : builder.lessThanOrEqualTo(root.get("createDate"), endDate);
    }

    public static Specification<Order> hasPriceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, builder) ->
                minPrice == null ? null : builder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Order> hasPriceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, builder) ->
                maxPrice == null ? null : builder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Order> hasSizeGreaterThanOrEqual(BigDecimal minSize) {
        return (root, query, builder) ->
                minSize == null ? null : builder.greaterThanOrEqualTo(root.get("size"), minSize);
    }

    public static Specification<Order> hasSizeLessThanOrEqual(BigDecimal maxSize) {
        return (root, query, builder) ->
                maxSize == null ? null : builder.lessThanOrEqualTo(root.get("size"), maxSize);
    }
}
