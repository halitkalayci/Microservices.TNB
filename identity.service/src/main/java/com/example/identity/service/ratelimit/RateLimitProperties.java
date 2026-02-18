package com.example.identity.service.ratelimit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    /** Kova kapasitesi (maksimum token sayısı) */
    private int capacity = 25;
    /** Dakikada dolan token sayısı (refill) */
    private int refillPerMinute = 5;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRefillPerMinute() {
        return refillPerMinute;
    }

    public void setRefillPerMinute(int refillPerMinute) {
        this.refillPerMinute = refillPerMinute;
    }
}
