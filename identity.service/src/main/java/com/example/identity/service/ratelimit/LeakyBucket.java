package com.example.identity.service.ratelimit;

/**
 * Thread-safe leaky bucket: kapasite kadar token, dakikada refillRate kadar token dolar.
 * Her istek 1 token tüketir; token yoksa istek reddedilir.
 */
public class LeakyBucket {

    private final double capacity;
    private final double refillPerMinute;
    private double tokens;
    private long lastRefillTimeMillis;

    public LeakyBucket(double capacity, double refillPerMinute) {
        this.capacity = capacity;
        this.refillPerMinute = refillPerMinute;
        this.tokens = capacity;
        this.lastRefillTimeMillis = System.currentTimeMillis();
    }

    /**
     * 1 token tüketmeyi dene. Yeterli token varsa true, yoksa false döner.
     */
    public synchronized boolean tryConsume() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - lastRefillTimeMillis;
        double elapsedMinutes = elapsedMillis / 60_000.0;
        double toAdd = elapsedMinutes * refillPerMinute;
        tokens = Math.min(capacity, tokens + toAdd);
        lastRefillTimeMillis = now;
    }
}
