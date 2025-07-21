
package com.example.model;

import com.example.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerFieldsAndBuilder() {
        String externalId = UUID.randomUUID().toString();

        Customer customer = Customer.builder()
                .id(1L)
                .username("testuser")
                .password("secret")
                .role(Role.CUSTOMER)
                .externalId(externalId)
                .build();

        assertEquals(1L, customer.getId());
        assertEquals("testuser", customer.getUsername());
        assertEquals("secret", customer.getPassword());
        assertEquals(Role.CUSTOMER, customer.getRole());
        assertEquals(externalId, customer.getExternalId());
    }

    @Test
    void testAuthoritiesContainsCorrectRole() {
        Customer customer = Customer.builder()
                .username("admin")
                .password("pass")
                .role(Role.ADMIN)
                .build();

        Collection<? extends GrantedAuthority> authorities = customer.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void testAccountStatusMethods() {
        Customer customer = new Customer();

        assertTrue(customer.isAccountNonExpired());
        assertTrue(customer.isAccountNonLocked());
        assertTrue(customer.isCredentialsNonExpired());
        assertTrue(customer.isEnabled());
    }

    @Test
    void testNoArgsConstructorInitializesExternalId() {
        Customer customer = new Customer();
        assertNotNull(customer.getExternalId());
    }
}
