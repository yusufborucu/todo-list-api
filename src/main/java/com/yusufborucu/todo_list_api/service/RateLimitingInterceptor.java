package com.yusufborucu.todo_list_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/swagger-ui/") || uri.startsWith("/v3/api-docs/") || uri.startsWith("/swagger-resources/") || uri.startsWith("/webjars/")) {
            return true;
        }

        if (!rateLimiterService.isAllowed(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Too many requests. Please try again later.");
            return false;
        }
        return true;
    }
}
