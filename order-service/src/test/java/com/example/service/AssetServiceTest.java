
package com.example.service;

import com.example.model.Asset;
import com.example.model.Customer;
import com.example.repository.AssetRepository;
import com.example.repository.CustomerRepository;
import com.example.response.AssetResponse;
import com.example.util.AssetMapper;
import com.example.util.AuthUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceTest {

    @InjectMocks
    private AssetService assetService;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAssets_Success() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);

        Asset asset = new Asset();
        asset.setAssetName("BTC");
        asset.setSize(new BigDecimal("2"));
        asset.setUsableSize(new BigDecimal("1.5"));
        asset.setCustomer(customer);

        AssetResponse response = AssetResponse.builder()
                .assetName("BTC")
                .size(new BigDecimal("2"))
                .usableSize(new BigDecimal("1.5"))
                .customerId(customerId)
                .build();

        when(authUtil.isAdminOrAccessingOwnData(customerId)).thenReturn(true);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(assetRepository.getAssetsByCustomerId(customerId)).thenReturn(Optional.of(List.of(asset)));
        when(assetMapper.fromAsset(asset)).thenReturn(response);

        List<AssetResponse> result = assetService.listAssets(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BTC", result.getFirst().getAssetName());
    }

    @Test
    void testListAssets_AccessDenied() {
        Long customerId = 1L;
        when(authUtil.isAdminOrAccessingOwnData(customerId)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> assetService.listAssets(customerId));
    }

    @Test
    void testListAssets_CustomerNotFound() {
        Long customerId = 1L;
        when(authUtil.isAdminOrAccessingOwnData(customerId)).thenReturn(true);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> assetService.listAssets(customerId));
    }

    @Test
    void testListAssets_AssetsEmpty() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);

        when(authUtil.isAdminOrAccessingOwnData(customerId)).thenReturn(true);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(assetRepository.getAssetsByCustomerId(customerId)).thenReturn(Optional.of(Collections.emptyList()));

        assertThrows(EntityNotFoundException.class, () -> assetService.listAssets(customerId));
    }

    @Test
    void testListAssets_OptionalEmpty() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);

        when(authUtil.isAdminOrAccessingOwnData(customerId)).thenReturn(true);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(assetRepository.getAssetsByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> assetService.listAssets(customerId));
    }
}
