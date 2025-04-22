package com.monkey.storereservationservice.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserContextFilter extends OncePerRequestFilter {

    private final UserContext userContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String userName = request.getHeader("X-User-Name");
        String userRole = request.getHeader("X-User-Role");

        if (userId != null) {
            userContext.setUserId(Long.valueOf(userId));
        }
        if (userName != null) {
            userContext.setUserName(userName);
        }
        if (userRole != null) {
            userContext.setUserRole(userRole);
        }

        filterChain.doFilter(request, response);
    }
}