package com.example.service;

import com.example.model.Asset;
import com.example.repository.AssetRepository;
import com.example.repository.specification.AssetSpecification;
import com.example.response.AssetResponse;
import com.example.util.AssetMapper;
import com.example.util.AuthUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {

    final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final AuthUtil authUtil;

    public List<AssetResponse> listAssets(Long customerId,
                                          String assetName,
                                          BigDecimal minSize,
                                          BigDecimal maxSize,
                                          BigDecimal minUsableSize,
                                          BigDecimal maxUsableSize) {


        if (customerId != null && !authUtil.isAdminOrAccessingOwnData(customerId)) {
            throw new AccessDeniedException("You can only view your own assets");
        }

        if (!authUtil.isAdmin()) {
            customerId = authUtil.getCustomerId();
        }

        Specification<Asset> spec = Specification
                .where(AssetSpecification.hasCustomerId(customerId))
                .and(AssetSpecification.hasAssetName(assetName))
                .and(AssetSpecification.hasSizeGreaterThanOrEqual(minSize))
                .and(AssetSpecification.hasSizeLessThanOrEqual(maxSize))
                .and(AssetSpecification.hasUsableSizeGreaterThanOrEqual(minUsableSize))
                .and(AssetSpecification.hasUsableSizeLessThanOrEqual(maxUsableSize));

        List<Asset> assets = assetRepository.findAll(spec);

        if (assets.isEmpty()) {
            throw new EntityNotFoundException("No assets found for given criteria.");
        }

        return assets.stream().map(assetMapper::fromAsset).collect(Collectors.toList());
    }

}


