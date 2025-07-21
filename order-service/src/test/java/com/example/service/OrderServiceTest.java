
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrders_AccessDenied() {
        when(authUtil.isAdminOrAccessingOwnData(1L)).thenReturn(false);
        assertThrows(AccessDeniedException.class, () ->
                orderService.getOrders(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now()));
    }

    @Test
    void testCreateOrder_BuyOrder_Success() {
        OrderDto dto = new OrderDto();
        dto.setCustomerId(1L);
        dto.setAssetName("BTC");
        dto.setSide(OrderSide.BUY);
        dto.setPrice(new BigDecimal("100"));
        dto.setSize(new BigDecimal("2"));

        Asset btcAsset = new Asset();
        btcAsset.setAssetName("BTC");
        Asset tryAsset = new Asset();
        tryAsset.setAssetName("TRY");
        tryAsset.setUsableSize(new BigDecimal("500"));
        Customer customer = new Customer();

        when(authUtil.isAdminOrAccessingOwnData(1L)).thenReturn(true);
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.of(btcAsset));
        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(Optional.of(tryAsset));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Order order = new Order();
        when(orderMapper.toOrder(dto, btcAsset, customer)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.fromOrder(order)).thenReturn(new OrderResponse());

        OrderResponse response = orderService.createOrder(dto);
        assertNotNull(response);
    }

    @Test
    void testDeleteOrder_OrderNotFound() {
        when(authUtil.isAdminOrAccessingOwnData(1L)).thenReturn(true);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void testMatchOrder_OrderNotPending() {
        Order order = new Order();
        order.setStatus(OrderStatus.CANCELED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.matchOrder(1L));
    }

    @Test
    void testMatchOrder_BuyOrder_Success() {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setOrderSide(OrderSide.BUY);
        order.setPrice(new BigDecimal("100"));
        order.setSize(new BigDecimal("1"));

        Asset tryAsset = new Asset();
        tryAsset.setAssetName("TRY");
        tryAsset.setUsableSize(new BigDecimal("200"));
        Asset tradedAsset = new Asset();
        tradedAsset.setAssetName("BTC");
        tradedAsset.setSize(BigDecimal.ZERO);
        tradedAsset.setUsableSize(BigDecimal.ZERO);

        Customer customer = new Customer();
        customer.setId(1L);
        order.setCustomer(customer);

        Asset orderAsset = new Asset(); orderAsset.setAssetName("BTC");
        order.setAsset(orderAsset);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(Optional.of(tryAsset));
        when(assetRepository.findByCustomerIdAndAssetName(1L, "BTC")).thenReturn(Optional.of(tradedAsset));

        orderService.matchOrder(1L);

        assertEquals(OrderStatus.MATCHED, order.getStatus());
        verify(orderRepository).save(order);
        verify(assetRepository).saveAll(anyList());
    }
}
