package com.cajaviva.cajaviva.support;

import com.cajaviva.cajaviva.auth.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;

public class WithAuthenticatedUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthenticatedUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthenticatedUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserPrincipal principal = new UserPrincipal(
                UUID.fromString(annotation.userId()),
                "test@test.com",
                true
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );
        context.setAuthentication(auth);
        return context;
    }
}
