package com.example.util;

import com.example.model.Asset;
import com.example.response.AssetResponse;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper {

    public AssetResponse fromAsset(Asset asset) {
        if (asset == null) {
            return null;
        }
        return com.example.response.AssetResponse.builder()
                .id(asset.getId())
                .assetName(asset.getAssetName())
                .size(asset.getSize())
                .usableSize(asset.getUsableSize())
                .customerId(asset.getCustomer().getId())
                .build();
    }
}
