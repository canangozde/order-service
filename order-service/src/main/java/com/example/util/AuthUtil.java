package com.example.util;

import com.example.enums.Role;
import com.example.model.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public UserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    public Long getCustomerId() {
        return ((Customer) getCurrentUser()).getId();
    }

    public boolean isAdminOrAccessingOwnData(Long customerId) {
        return isAdmin() || isAccessingOwnData(customerId);
    }

    public boolean isAdmin() {
        return getCurrentUser().getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + Role.ADMIN.name()));
    }

    public boolean isAccessingOwnData(Long customerId) {
        return getCustomerId().equals(customerId);
    }
}

