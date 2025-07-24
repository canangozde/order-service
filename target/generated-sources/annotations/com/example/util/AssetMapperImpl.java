package com.example.util;

import com.example.model.Asset;
import com.example.response.AssetResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-24T11:43:04+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class AssetMapperImpl implements AssetMapper {

    @Override
    public AssetResponse fromAsset(Asset asset) {
        if ( asset == null ) {
            return null;
        }

        AssetResponse.AssetResponseBuilder assetResponse = AssetResponse.builder();

        assetResponse.customerId( AssetMapper.mapCustomerToId( asset.getCustomer() ) );
        assetResponse.id( asset.getId() );
        assetResponse.assetName( asset.getAssetName() );
        assetResponse.size( asset.getSize() );
        assetResponse.usableSize( asset.getUsableSize() );

        return assetResponse.build();
    }
}
