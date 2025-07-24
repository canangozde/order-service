
package com.example.util;

import com.example.model.Asset;
import com.example.model.Customer;
import com.example.response.AssetResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AssetMapperTest {

    private final AssetMapper assetMapper = Mappers.getMapper(AssetMapper.class);

    @Test
    void testFromAsset_ValidAsset() {
        Customer customer = new Customer();
        customer.setId(1L);

        Asset asset = new Asset();
        asset.setId(100L);
        asset.setAssetName("BTC");
        asset.setSize(new BigDecimal("2.5"));
        asset.setUsableSize(new BigDecimal("1.5"));
        asset.setCustomer(customer);

        AssetResponse response = assetMapper.fromAsset(asset);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("BTC", response.getAssetName());
        assertEquals(new BigDecimal("2.5"), response.getSize());
        assertEquals(new BigDecimal("1.5"), response.getUsableSize());
        assertEquals(1L, response.getCustomerId());
    }

    @Test
    void testFromAsset_NullAsset() {
        AssetResponse response = assetMapper.fromAsset(null);
        assertNull(response);
    }
}
