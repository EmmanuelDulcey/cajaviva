package com.cajaviva.cajaviva.utilities;

import com.cajaviva.cajaviva.auth.security.UserPrincipal;
import com.cajaviva.cajaviva.exception.AuthenticationFailedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthenticationFailedException("User not authenticated");
        }
        return principal.getUserId();
    }
}
