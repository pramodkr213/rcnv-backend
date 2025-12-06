package com.main.jobilitybackend.jwtSecurity;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidator extends OncePerRequestFilter {

    private final CustomAdminDetail adminDetail;
    private final CustomEmployerDetail employerDetail;
    private final CustomStudentDetail studentDetail;

    public JwtValidator(
            CustomAdminDetail adminDetail,
            CustomEmployerDetail employerDetail,
            CustomStudentDetail studentDetail) {
        this.adminDetail = adminDetail;
        this.employerDetail = employerDetail;
        this.studentDetail = studentDetail;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                String email = JwtProvider.getEmailFromJwt(token);

                // Determine user role and load correct user detail
                String path = request.getServletPath();
                UserDetails userDetails = null;

                if (path.startsWith("/api/admin")) {
                    userDetails = adminDetail.loadUserByUsername(email);
                } else if (path.startsWith("/api/employer")) {
                    userDetails = employerDetail.loadUserByUsername(email);
                } else if (path.startsWith("/api/student")) {
                    userDetails = studentDetail.loadUserByUsername(email);
                }

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Token is invalid or expired. Please login again.\"}");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Token is missing. Please login.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
