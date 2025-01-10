package com.vls.tristar.chat.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String transactionId = UUID.randomUUID().toString().toLowerCase();
        MDC.put("transaction-id", transactionId);
        MDC.put("request-id", request.getRequestId());
        response.setHeader("transaction-id", transactionId);
        filterChain.doFilter(request, response);
        MDC.clear();
    }
}
