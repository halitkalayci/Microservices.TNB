package com.example.identity.service.configuration;

import com.example.identity.service.ratelimit.LeakyBucketRateLimiter;
import com.example.identity.service.ratelimit.RateLimitFilter;
import com.example.identity.service.ratelimit.RateLimitPathMatcher;
import com.example.identity.service.ratelimit.RateLimitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfiguration {

    @Bean
    public RateLimitPathMatcher rateLimitPathMatcher() {
        return new RateLimitPathMatcher();
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter(LeakyBucketRateLimiter rateLimiter,
                                                                     RateLimitPathMatcher pathMatcher) {
        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitFilter(rateLimiter, pathMatcher));
        registration.addUrlPatterns("/*");
        registration.setOrder(-100);
        return registration;
    }
}
