package com.melog.clearpass.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class M2MAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(M2MAuthFilter.class);

    @Value("${user.service.m2m.token}")
    private String m2mToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        
        logger.info("M2MAuthFilter: Processing request {} {} from {}", method, requestPath, request.getRemoteAddr());
        
        // Only apply M2M validation to internal endpoints
        if (requestPath.startsWith("/api/users/internal")) {
            logger.info("M2MAuthFilter: Internal endpoint detected, checking M2M token");
            
            String serviceToken = request.getHeader("X-Service-Token");
            String obscuredExpected = (m2mToken != null && m2mToken.length() > 6) ? m2mToken.substring(0,3) + "..." + m2mToken.substring(m2mToken.length()-3) : m2mToken;
            String obscuredReceived = (serviceToken != null && serviceToken.length() > 6) ? serviceToken.substring(0,3) + "..." + serviceToken.substring(serviceToken.length()-3) : serviceToken;
            logger.info("M2MAuthFilter: Expected M2M token: '{}'", obscuredExpected);
            logger.info("M2MAuthFilter: Received M2M token: '{}'", obscuredReceived);
            logger.info("M2MAuthFilter: Tokens match: {}", serviceToken != null && serviceToken.equals(m2mToken));
            
            if (serviceToken != null && serviceToken.equals(m2mToken)) {
                // Valid M2M token - create authentication
                logger.info("M2MAuthFilter: Valid M2M token, creating authentication");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    "service",
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_SERVICE"))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("M2MAuthFilter: Authentication set successfully");
            } else {
                // Invalid or missing M2M token
                logger.warn("M2MAuthFilter: Invalid or missing M2M token, returning 401");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            logger.info("M2MAuthFilter: Non-internal endpoint, skipping M2M validation");
        }

        logger.info("M2MAuthFilter: Proceeding to next filter");
        filterChain.doFilter(request, response);
    }
} 