
package com.example.util;

import com.example.enums.Role;
import com.example.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthUtilTest {

    private AuthUtil authUtil;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil();
    }

    @Test
    void testGetCurrentUser_ReturnsCustomer() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(123L);
        mockCustomer.setRole(Role.CUSTOMER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockCustomer);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertEquals(mockCustomer, authUtil.getCurrentUser());
    }

    @Test
    void testGetCustomerId_ReturnsId() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(456L);
        mockCustomer.setRole(Role.CUSTOMER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockCustomer);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertEquals(456L, authUtil.getCustomerId());
    }

    @Test
    void testIsAdmin_ReturnsTrue() {
        Customer admin = new Customer();
        admin.setRole(Role.ADMIN);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(admin);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertTrue(authUtil.isAdmin());
    }

    @Test
    void testIsAdmin_ReturnsFalse() {
        Customer user = new Customer();
        user.setRole(Role.CUSTOMER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertFalse(authUtil.isAdmin());
    }

    @Test
    void testIsAdminOrAccessingOwnData_Admin() {
        Customer admin = new Customer();
        admin.setId(999L);
        admin.setRole(Role.ADMIN);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(admin);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertTrue(authUtil.isAdminOrAccessingOwnData(123L));
    }

    @Test
    void testIsAdminOrAccessingOwnData_OwnData() {
        Customer user = new Customer();
        user.setId(123L);
        user.setRole(Role.CUSTOMER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertTrue(authUtil.isAdminOrAccessingOwnData(123L));
    }

    @Test
    void testIsAdminOrAccessingOwnData_Denied() {
        Customer user = new Customer();
        user.setId(456L);
        user.setRole(Role.CUSTOMER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        assertFalse(authUtil.isAdminOrAccessingOwnData(123L));
    }
}
