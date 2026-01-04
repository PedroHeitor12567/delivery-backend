package com.pedroferreira.deliveryapplication.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de Segurança
 *
 * ATENÇÃO: Esta configuração está LIBERANDO TUDO para desenvolvimento.
 * Em produção, você deve implementar autenticação e autorização adequadas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF (necessário para APIs REST stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Configura autorização de requisições
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI e OpenAPI docs - PÚBLICO
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // H2 Console - PÚBLICO (apenas para desenvolvimento)
                        .requestMatchers("/h2-console/**").permitAll()

                        // Endpoints da API - PÚBLICO (temporário para desenvolvimento)
                        .requestMatchers("/api/**").permitAll()

                        // Qualquer outra requisição - PÚBLICO (temporário)
                        .anyRequest().permitAll()
                )

                // Desabilita gerenciamento de sessão (API stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Permite frames do H2 Console
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }
}