package com.product.commerce.service;

import com.product.commerce.config.RateLimitConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RateLimiterService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String username) {

        String key = "rate_limit:user:" + username;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            // First request → set TTL
            redisTemplate.expire(
                    key,
                    Duration.ofSeconds(RateLimitConfig.WINDOW_SECONDS)
            );
        }

        return count <= RateLimitConfig.MAX_ORDERS_PER_MINUTE;
    }
}
