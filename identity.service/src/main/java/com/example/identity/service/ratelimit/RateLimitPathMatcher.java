package com.example.identity.service.ratelimit;

import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * Hangi path'lerin rate limit'e tabi olacağını belirler. Actuator, Swagger, H2 console hariç tutulur.
 */
public class RateLimitPathMatcher {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    private static final List<String> EXCLUDED_PATTERNS = List.of(
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/h2-console/**"
    );

    public boolean shouldRateLimit(String path) {
        if (path == null) return false;
        for (String pattern : EXCLUDED_PATTERNS) {
            if (MATCHER.match(pattern, path)) {
                return false;
            }
        }
        return true;
    }
}
