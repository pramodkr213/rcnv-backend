package com.main.jobilitybackend.logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException{
        String requestDetails = String.format("REQUEST: %s %s from IP %s",
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        logger.info(requestDetails);

        filterChain.doFilter(request, response);

        String responseDetails = String.format("RESPONSE: %s %s -> %d",
                request.getMethod(), request.getRequestURI(), response.getStatus());

        logger.info(responseDetails);
    }
}
