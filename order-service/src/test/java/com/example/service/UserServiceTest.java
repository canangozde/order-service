
package com.example.service;

import com.example.enums.Role;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UserService userService;

    private Customer user;

    @BeforeEach
    void setUp() {
        user = new Customer();
        user.setUsername("dummyUser");
        user.setPassword("dummyHashedPassword");
        user.setRole(Role.CUSTOMER);
    }

    @Test
    void shouldReturnUserDetailsWhenUserExists() {
        when(customerRepository.findByUsername("dummyUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.userDetailService().loadUserByUsername("dummyUser");

        assertNotNull(userDetails);
        assertEquals("dummyUser", userDetails.getUsername());
        assertEquals("dummyHashedPassword", userDetails.getPassword());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        when(customerRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.userDetailService().loadUserByUsername("unknown"));
    }
}
