
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
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

    private static final Long CUSTOMER_ID = 1L;

    private List<AssetResponse> callListAssets() {
        return assetService.listAssets(CUSTOMER_ID, "BTC",
                new BigDecimal("1"), new BigDecimal("3"),
                new BigDecimal("1"), new BigDecimal("2"));
    }

    @Test
    void testListAssets_Success() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);

        Asset asset = new Asset();
        asset.setAssetName("BTC");
        asset.setSize(new BigDecimal("2"));
        asset.setUsableSize(new BigDecimal("1.5"));
        asset.setCustomer(customer);

        AssetResponse response = AssetResponse.builder()
                .assetName("BTC")
                .size(new BigDecimal("2"))
                .usableSize(new BigDecimal("1.5"))
                .customerId(CUSTOMER_ID)
                .build();

        when(authUtil.isAdmin()).thenReturn(true);
        when(authUtil.isAdminOrAccessingOwnData(CUSTOMER_ID)).thenReturn(true);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(assetRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(asset));
        when(assetMapper.fromAsset(asset)).thenReturn(response);

        List<AssetResponse> result = callListAssets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BTC", result.getFirst().getAssetName());
    }

    @Test
    void testListAssets_AccessDenied() {
        when(authUtil.isAdmin()).thenReturn(false);
        when(authUtil.isAdminOrAccessingOwnData(CUSTOMER_ID)).thenReturn(false);
        when(authUtil.getCustomerId()).thenReturn(999L); // Simulate a different customer ID

        assertThrows(AccessDeniedException.class, () ->
                assetService.listAssets(CUSTOMER_ID, null, null, null, null, null));

        verify(assetRepository, never()).findAll(any(Specification.class));
    }


    @Test
    void testListAssets_CustomerNotFound() {
        when(authUtil.isAdmin()).thenReturn(true);
        when(authUtil.isAdminOrAccessingOwnData(CUSTOMER_ID)).thenReturn(true);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                assetService.listAssets(CUSTOMER_ID, null, null, null, null, null));
    }

    @Test
    void testListAssets_EmptyAssetList() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);

        when(authUtil.isAdmin()).thenReturn(true);
        when(authUtil.isAdminOrAccessingOwnData(CUSTOMER_ID)).thenReturn(true);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(assetRepository.findAll(any(Specification.class))).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> callListAssets());
    }
}
