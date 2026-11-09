
package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtFilter) throws Exception {

        http
            // Disable CSRF for stateless REST APIs
            .csrf(csrf -> csrf.disable())

            // Use JWT instead of HTTP sessions
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // API authorization rules
            .authorizeHttpRequests(auth -> auth

                // 1. Public customer registration
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/customers/register"
                ).permitAll()

                // 2. Public worker registration
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/workers/register"
                ).permitAll()

                // 3. Public login
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/login"
                ).permitAll()

                // 4. Admin registration
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/admins/register"
                ).hasRole("ADMIN")

                // 5. Authenticated user profile
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/auth/me"
                ).authenticated()

                // 6. Admin worker management
                .requestMatchers(
                    "/api/admin/workers/**"
                ).hasRole("ADMIN")

                // 7. Public service category viewing
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/categories",
                    "/api/categories/**"
                ).permitAll()

                // 8. Admin category management
                .requestMatchers(
                    "/api/admin/categories",
                    "/api/admin/categories/**"
                ).hasRole("ADMIN")

                // 9. Allow Spring error endpoint
                .requestMatchers("/error").permitAll()

                // 10. Secure all remaining endpoints
                .anyRequest().authenticated()
            )

            // Allow bootstrap admin Basic Authentication
            .httpBasic(basic -> {})

            // Authenticate JWT Bearer tokens
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // Bootstrap admin for initial setup
    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        String username = System.getenv(
            "SMARTSERVE_BOOTSTRAP_ADMIN_USER"
        );

        String password = System.getenv(
            "SMARTSERVE_BOOTSTRAP_ADMIN_PASSWORD"
        );

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            throw new IllegalStateException(
                "Set bootstrap admin username and password"
            );
        }

        return new InMemoryUserDetailsManager(
            User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("ADMIN")
                .build()
        );
    }
}
