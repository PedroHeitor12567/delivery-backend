package com.pedroferreira.deliveryapplication.infrastructure.config;

import com.pedroferreira.deliveryapplication.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**",
                                "/api/auth/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/customers/register").permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/stores",
                                "/api/stores/*",
                                "/api/stores/open",
                                "/api/stores/category/**",
                                "/api/stores/search",
                                "/api/products/store/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/customers/**",
                                "/api/orders"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/orders/customer/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/*/cancel").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/sellers/apply").hasRole("CUSTOMER")

                        .requestMatchers("/api/sellers/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/stores/*/open",
                                "/api/stores/*/close"
                        ).hasRole("SELLER")

                        .requestMatchers(HttpMethod.GET, "/api/orders/store/**").hasRole("SELLER")
                        .requestMatchers("/api/products/**").hasRole("SELLER")

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}