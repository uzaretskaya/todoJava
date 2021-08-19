package ru.uzaretskaya.todo.auth.utils;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    private final String ACCESS_TOKEN = "access_token";

    @Value("${cookie.jwt.max-age}")
    private int cookieAccessTokenDuration;

    @Value("${cookie.domain}")
    private String cookieAccessTokenDomain;

    public HttpCookie createJwtCookie(String jwt) {
        return ResponseCookie.from(ACCESS_TOKEN, jwt)
                .maxAge(cookieAccessTokenDuration)
                .sameSite(SameSiteCookies.STRICT.getValue())
                .httpOnly(true)
                .secure(true)
                .domain(cookieAccessTokenDomain)
                .path("/")
                .build();
    }
}
