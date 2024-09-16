package com.yusufborucu.todo_list_api.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public final int MAX_TOKENS = 10;
    public final int REFILL_RATE = 1;
    public final long REFILL_TIME = 60;
    private final String IP_SET_KEY = "rate_limit_ips";

    public boolean isAllowed(HttpServletRequest request) {
        String ipAddress = getClientIP(request);
        String tokenKey = "rate_limit:" + ipAddress;

        redisTemplate.opsForSet().add(IP_SET_KEY, ipAddress);

        String tokens = redisTemplate.opsForValue().get(tokenKey);
        int currentTokens = tokens == null ? MAX_TOKENS : Integer.parseInt(tokens);

        if (currentTokens > 0) {
            redisTemplate.opsForValue().set(tokenKey, String.valueOf(currentTokens - 1), REFILL_TIME, TimeUnit.SECONDS);
            return true;
        } else {
            return false;
        }
    }

    public void refillTokens(String ipAddress) {
        String tokenKey = "rate_limit:" + ipAddress;

        String tokens = redisTemplate.opsForValue().get(tokenKey);
        int currentTokens = tokens == null ? MAX_TOKENS : Integer.parseInt(tokens);

        if (currentTokens < MAX_TOKENS) {
            redisTemplate.opsForValue().set(tokenKey, String.valueOf(currentTokens + REFILL_RATE), REFILL_TIME, TimeUnit.SECONDS);
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
