package com.javathinked.example.demo_spring.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigSimpleTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(jwtAuthFilter);
    }

    @Test
    void shouldCreateSecurityConfig() {
        // Given & When
        SecurityConfig config = new SecurityConfig(jwtAuthFilter);

        // Then
        assertNotNull(config);
    }

    @Test
    void shouldCreatePasswordEncoder() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void shouldCreateCorsConfigurationSource() {
        // When
        var corsConfigurationSource = securityConfig.corsConfigurationSource();

        // Then
        assertNotNull(corsConfigurationSource);
    }
}
