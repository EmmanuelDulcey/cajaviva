package com.cajaviva.cajaviva.support;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthenticatedUserSecurityContextFactory.class)
public @interface WithAuthenticatedUser {
    String userId() default "00000000-0000-0000-0000-000000000001";
}
