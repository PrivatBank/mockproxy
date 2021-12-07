package com.pb.mockproxy.filter;

import com.google.common.base.Strings;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class RayIdLoggingFilter extends OncePerRequestFilter {

    public static final String RAY_ID_HTTP_HEADER = "X-Ray-Id";
    public static final String RAY_ID_MDC_KEY = "rayId";
    public static final String RAY_ID_REQUEST_ATTR_KEY = "RayIdLoggingFilter.rayId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String rayId = request.getHeader(RAY_ID_HTTP_HEADER);
        if(Strings.isNullOrEmpty(rayId)){
            rayId = UUID.randomUUID().toString().replace("-", "");
        }
        request.setAttribute(RAY_ID_REQUEST_ATTR_KEY, rayId);
        response.addHeader(RAY_ID_HTTP_HEADER, rayId);
        MDC.put(RAY_ID_MDC_KEY, rayId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(RAY_ID_MDC_KEY);
        }
    }
}
