package com.yusufborucu.todo_list_api.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class TokenRefillScheduler {

    private final RateLimiterService rateLimiterService;
    private final StringRedisTemplate redisTemplate;

    public TokenRefillScheduler(RateLimiterService rateLimiterService, StringRedisTemplate redisTemplate) {
        this.rateLimiterService = rateLimiterService;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 60000)
    public void refillTokensForAllClients() {
        System.out.println("REFILL LOG");
        Set<String> ipAddresses = redisTemplate.opsForSet().members("rate_limit_ips");

        for (String ipAddress : ipAddresses) {
            String tokenKey = "rate_limit:" + ipAddress;
            String tokens = redisTemplate.opsForValue().get(tokenKey);
            int currentTokens = tokens == null ? 0 : Integer.parseInt(tokens);

            if (currentTokens < rateLimiterService.MAX_TOKENS) {
                redisTemplate.opsForValue().set(tokenKey, String.valueOf(currentTokens + rateLimiterService.REFILL_RATE), rateLimiterService.REFILL_TIME, TimeUnit.SECONDS);
            }
        }
    }
}
