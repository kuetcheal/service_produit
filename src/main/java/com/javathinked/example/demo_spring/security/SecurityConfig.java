package com.javathinked.example.demo_spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Active CORS (utilisera le bean corsConfigurationSource ci-dessous)
            .cors(c -> c.configurationSource(corsConfigurationSource()))
            // Désactive CSRF pour une API stateless
            .csrf(csrf -> csrf.disable())
            // JWT = stateless
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Règles d'accès
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/actuator/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            )
            // Filtre JWT
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuration CORS pour autoriser ton front Nuxt (dev: http://localhost:3000).
     * Adapte en prod (ex: https://app.tondomaine.com) ou utilise AllowedOriginPatterns.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();

        // ORIGINES AUTORISÉES
        cors.setAllowedOrigins(List.of(
            "http://localhost:3000"   // Nuxt dev
            // "https://app.tondomaine.com" // <- à activer/ajouter pour la prod
        ));
        // Si tu veux permettre des ports variables en dev :
        // cors.setAllowedOriginPatterns(List.of("http://localhost:*"));

        // MÉTHODES AUTORISÉES
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // HEADERS AUTORISÉS (ce que le navigateur peut envoyer)
        cors.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));

        // HEADERS EXPOSES (lisibles côté navigateur)
        cors.setExposedHeaders(List.of("Location", "Authorization"));

        // Cookies/credentials si besoin (laisse à false si tu n’en utilises pas)
        cors.setAllowCredentials(true);

        // Cache des préflight (en secondes)
        cors.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
