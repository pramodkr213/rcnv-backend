package com.main.jobilitybackend.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.main.jobilitybackend.jwtSecurity.CustomAdminDetail;
import com.main.jobilitybackend.jwtSecurity.CustomEmployerDetail;
import com.main.jobilitybackend.jwtSecurity.CustomStudentDetail;
import com.main.jobilitybackend.jwtSecurity.JwtValidator;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private CustomStudentDetail studentDetailsService;

        @Autowired
        private CustomAdminDetail adminDetailsService;

        @Autowired
        private CustomEmployerDetail employerDetailsService;

        @Autowired
        private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

        @Autowired
        private CustomOAuth2UserService customOAuth2UserService;

        @Value("${app.cors.allowed-origins}")
        private String allowedOrigins;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // Admin Security
        @Bean
        @Order(1)
        public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/admin/**")
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN"))
                                .addFilterBefore(
                                                new JwtValidator(adminDetailsService, employerDetailsService,
                                                                studentDetailsService),
                                                BasicAuthenticationFilter.class)
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authenticationProvider(adminAuthenticationProvider());
                return http.build();
        }

        @Bean
        public AuthenticationProvider adminAuthenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(adminDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
        }

        // Student Security
        @Bean
        @Order(2)
        public SecurityFilterChain studentSecurityFilterChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/student/**")
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/student/**").hasRole("STUDENT"))
                                .addFilterBefore(
                                                new JwtValidator(adminDetailsService, employerDetailsService,
                                                                studentDetailsService),
                                                BasicAuthenticationFilter.class)
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authenticationProvider(studentAuthenticationProvider());
                return http.build();
        }

        @Bean
        public AuthenticationProvider studentAuthenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(studentDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
        }

        // Employer Security
        @Bean
        @Order(3)
        public SecurityFilterChain employerSecurityFilterChain(HttpSecurity http) throws Exception {
                http.securityMatcher("/api/employer/**")
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/employer/**").hasRole("EMPLOYER"))
                                .addFilterBefore(
                                                new JwtValidator(adminDetailsService, employerDetailsService,
                                                                studentDetailsService),
                                                BasicAuthenticationFilter.class)
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authenticationProvider(employerAuthenticationProvider());
                return http.build();
        }

        @Bean
        public AuthenticationProvider employerAuthenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(employerDetailsService);
                provider.setPasswordEncoder(passwordEncoder());
                return provider;
        }

        // Global Security for /auth/** and other URLs
        @Bean
        @Order(4)
        public SecurityFilterChain globalSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**",
                                                                "/api/public/**",
                                                                "/oauth2/**", "/api/user/**", "/uploads/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService)) // ✅ use your
                                                                                                       // service
                                                .successHandler(customOAuth2SuccessHandler))
                                // .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
                return http.build();
        }

        private CorsConfigurationSource corsConfigurationSource() {
                return request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Collections.singletonList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;

                };
        }
}
