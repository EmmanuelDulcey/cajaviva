package com.cajaviva.cajaviva.auth.service;

import com.cajaviva.cajaviva.auth.config.CookieProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {

    public static final String ACCESS_COOKIE = "CV_ACCESS_TOKEN";
    public static final String REFRESH_COOKIE = "CV_REFRESH_TOKEN";

    private final CookieProperties cookieProperties;

    public CookieService(CookieProperties cookieProperties) {
        this.cookieProperties = cookieProperties;
    }

    public void addAccessCookie(HttpServletResponse response, String token, Duration ttl) {
        addCookie(response, ACCESS_COOKIE, token, ttl);
    }

    public void addRefreshCookie(HttpServletResponse response, String token, Duration ttl) {
        addCookie(response, REFRESH_COOKIE, token, ttl);
    }

    public void clearAuthCookies(HttpServletResponse response) {
        addCookie(response, ACCESS_COOKIE, "", Duration.ZERO);
        addCookie(response, REFRESH_COOKIE, "", Duration.ZERO);
    }

    private void addCookie(HttpServletResponse response, String name, String value, Duration ttl) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .path(cookieProperties.getPath())
                .maxAge(ttl);

        if (cookieProperties.getDomain() != null && !cookieProperties.getDomain().isBlank()) {
            builder.domain(cookieProperties.getDomain());
        }

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }
}
