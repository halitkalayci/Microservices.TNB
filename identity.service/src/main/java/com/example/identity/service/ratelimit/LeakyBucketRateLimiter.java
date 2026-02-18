package com.example.identity.service.ratelimit;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * İstemci anahtarına (örn. IP) göre leaky-bucket rate limiting.
 */
@Component
public class LeakyBucketRateLimiter {

    private final RateLimitProperties properties;
    private final ConcurrentHashMap<String, LeakyBucket> buckets = new ConcurrentHashMap<>();

    public LeakyBucketRateLimiter(RateLimitProperties properties) {
        this.properties = properties;
    }

    /**
     * Verilen istemci için 1 token tüketmeyi dener. İzin varsa true, rate limit aşıldıysa false.
     */
    public boolean tryConsume(String clientKey) {
        LeakyBucket bucket = buckets.computeIfAbsent(clientKey,
                k -> new LeakyBucket(properties.getCapacity(), properties.getRefillPerMinute()));
        return bucket.tryConsume();
    }
}
