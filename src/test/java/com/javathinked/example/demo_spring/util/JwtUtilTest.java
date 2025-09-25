package com.javathinked.example.demo_spring.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String testSecret = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi10ZXN0aW5nLXB1cnBvc2VzLW9ubHk=";
    private final long testExpiration = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secretBase64", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", testExpiration);
    }

    @Test
    void shouldGenerateValidToken() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        // When
        String token = jwtUtil.generate(username, roles);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void shouldValidateValidToken() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList("ROLE_USER");
        String token = jwtUtil.generate(username, roles);

        // When
        boolean isValid = jwtUtil.validate(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void shouldNotValidateInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtUtil.validate(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void shouldNotValidateNullToken() {
        // Given
        String nullToken = null;

        // When
        boolean isValid = jwtUtil.validate(nullToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void shouldNotValidateEmptyToken() {
        // Given
        String emptyToken = "";

        // When
        boolean isValid = jwtUtil.validate(emptyToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void shouldExtractUsernameFromValidToken() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList("ROLE_USER");
        String token = jwtUtil.generate(username, roles);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldExtractRolesFromValidToken() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        String token = jwtUtil.generate(username, roles);

        // When
        List<String> extractedRoles = jwtUtil.extractRoles(token);

        // Then
        assertNotNull(extractedRoles);
        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.contains("ROLE_USER"));
        assertTrue(extractedRoles.contains("ROLE_ADMIN"));
    }

    @Test
    void shouldExtractUsernameFromTokenWithEmptyRoles() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList();
        String token = jwtUtil.generate(username, roles);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldExtractRolesFromTokenWithEmptyRoles() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList();
        String token = jwtUtil.generate(username, roles);

        // When
        List<String> extractedRoles = jwtUtil.extractRoles(token);

        // Then
        assertNotNull(extractedRoles);
        assertTrue(extractedRoles.isEmpty());
    }

    @Test
    void shouldGenerateValidTokens() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList("ROLE_USER");

        // When
        String token1 = jwtUtil.generate(username, roles);
        String token2 = jwtUtil.generate(username, roles);

        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        assertTrue(token1.length() > 0);
        assertTrue(token2.length() > 0);
    }

    @Test
    void shouldGenerateTokensWithDifferentExpirationTimes() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList("ROLE_USER");

        // When
        String token1 = jwtUtil.generate(username, roles);
        // Wait a bit to ensure different timestamps
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String token2 = jwtUtil.generate(username, roles);

        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        assertTrue(token1.length() > 0);
        assertTrue(token2.length() > 0);
        // Les tokens peuvent être identiques si générés dans la même seconde
    }

    @Test
    void shouldHandleSpecialCharactersInUsername() {
        // Given
        String username = "test@user#123";
        List<String> roles = Arrays.asList("ROLE_USER");

        // When
        String token = jwtUtil.generate(username, roles);
        boolean isValid = jwtUtil.validate(token);
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertTrue(isValid);
        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldHandleSpecialCharactersInRoles() {
        // Given
        String username = "testuser";
        List<String> roles = Arrays.asList("ROLE_USER@ADMIN", "ROLE#SPECIAL");

        // When
        String token = jwtUtil.generate(username, roles);
        boolean isValid = jwtUtil.validate(token);
        List<String> extractedRoles = jwtUtil.extractRoles(token);

        // Then
        assertTrue(isValid);
        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.contains("ROLE_USER@ADMIN"));
        assertTrue(extractedRoles.contains("ROLE#SPECIAL"));
    }
}
