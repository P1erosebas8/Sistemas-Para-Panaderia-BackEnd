package com.Sistemas_Para_Panaderia_BackEnd.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Endpoints de Swagger (Públicos)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        
                        // Endpoints de autenticación (Públicos)
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // Rutas estrictas de Administración (Protegidas por Rol ADMIN)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/images/**").hasRole("ADMIN")
                        
                        // Endpoints de lectura del catálogo y sedes (Públicos)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/branches/**").permitAll()
                        
                        // Endpoints de creación/edición de categorías que no usan /admin (Protegidos por Rol ADMIN)
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        
                        // Cualquier otra petición (crear orden, ver ordenes, cambiar perfil)
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
