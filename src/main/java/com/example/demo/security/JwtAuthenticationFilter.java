
package com.example.demo.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication
        .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority
        .SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                Claims claims =
                        jwtService.validateAndGetClaims(token);

                String subject = claims.getSubject();
                String role = claims.get("role", String.class);

                if (subject == null || role == null
                        || !List.of("CUSTOMER", "ADMIN", "WORKER")
                                .contains(role)
                        || !subject.matches(
                                "^(CUSTOMER|ADMIN|WORKER):[1-9][0-9]*$")
                        || !subject.startsWith(role + ":")) {

                    throw new IllegalArgumentException(
                            "Invalid token identity");
                }

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                subject,
                                null,
                                List.of(new SimpleGrantedAuthority(
                                        "ROLE_" + role)));

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);

            } catch (JwtException | IllegalArgumentException ex) {

                SecurityContextHolder.clearContext();

                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid or expired token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
