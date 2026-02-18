package com.example.identity.service.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Leaky-bucket rate limiting uygular. Limit aşılırsa 429 Too Many Requests döner.
 */
@Order(-100)
public class RateLimitFilter extends OncePerRequestFilter {

    private final LeakyBucketRateLimiter rateLimiter;
    private final RateLimitPathMatcher pathMatcher;

    public RateLimitFilter(LeakyBucketRateLimiter rateLimiter, RateLimitPathMatcher pathMatcher) {
        this.rateLimiter = rateLimiter;
        this.pathMatcher = pathMatcher;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!pathMatcher.shouldRateLimit(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientKey = resolveClientKey(request);
        if (rateLimiter.tryConsume(clientKey)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Rate limit aşıldı. Lütfen daha sonra tekrar deneyin.\"}");
        }
    }

    private String resolveClientKey(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr() != null ? request.getRemoteAddr() : "unknown";
    }
}
