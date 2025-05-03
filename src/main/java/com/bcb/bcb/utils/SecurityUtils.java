package com.bcb.bcb.utils;

import com.bcb.bcb.entity.Client;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static Long getUserId() {
        Authentication authentication = getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return Long.parseLong(userDetails.getUsername());
        } else if (principal instanceof Client client) {
            return client.getId();
        } else if (principal instanceof String) {
            return null;
        }

        return null;
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
