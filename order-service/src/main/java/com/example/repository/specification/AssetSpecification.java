package com.example.repository.specification;

import com.example.model.Asset;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class AssetSpecification {

    public static Specification<Asset> hasCustomerId(Long customerId) {
        return (root, query, cb) -> customerId == null ? null :
                cb.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<Asset> hasAssetName(String assetName) {
        return (root, query, cb) -> assetName == null ? null :
                cb.equal(cb.lower(root.get("assetName")), assetName.toLowerCase());
    }

    public static Specification<Asset> hasSizeGreaterThanOrEqual(BigDecimal minSize) {
        return (root, query, cb) -> minSize == null ? null :
                cb.greaterThanOrEqualTo(root.get("size"), minSize);
    }

    public static Specification<Asset> hasSizeLessThanOrEqual(BigDecimal maxSize) {
        return (root, query, cb) -> maxSize == null ? null :
                cb.lessThanOrEqualTo(root.get("size"), maxSize);
    }

    public static Specification<Asset> hasUsableSizeGreaterThanOrEqual(BigDecimal minUsableSize) {
        return (root, query, cb) -> minUsableSize == null ? null :
                cb.greaterThanOrEqualTo(root.get("usableSize"), minUsableSize);
    }

    public static Specification<Asset> hasUsableSizeLessThanOrEqual(BigDecimal maxUsableSize) {
        return (root, query, cb) -> maxUsableSize == null ? null :
                cb.lessThanOrEqualTo(root.get("usableSize"), maxUsableSize);
    }
}
