package com.javathinked.example.demo_spring.dto.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ClientCreatedEventTest {

    @Test
    void shouldCreateClientCreatedEventWithAllFields() {
        // Given
        Long clientId = 1L;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = 1;

        // When
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, createdAt, source, version);

        // Then
        assertNotNull(event);
        assertEquals(clientId, event.clientId());
        assertEquals(createdAt, event.createdAt());
        assertEquals(source, event.source());
        assertEquals(version, event.version());
    }

    @Test
    void shouldCreateClientCreatedEventWithNullFields() {
        // Given
        Long clientId = null;
        Instant createdAt = null;
        String source = null;
        int version = 0;

        // When
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, createdAt, source, version);

        // Then
        assertNotNull(event);
        assertNull(event.clientId());
        assertNull(event.createdAt());
        assertNull(event.source());
        assertEquals(0, event.version());
    }

    @Test
    void shouldReturnCorrectToString() {
        // Given
        Long clientId = 1L;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = 1;
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, createdAt, source, version);

        // When
        String toString = event.toString();

        // Then
        assertTrue(toString.contains("clientId=1"));
        assertTrue(toString.contains("source=service-client"));
        assertTrue(toString.contains("version=1"));
    }

    @Test
    void shouldBeEqualWhenContentsAreSame() {
        // Given
        Long clientId = 1L;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = 1;
        ClientCreatedEvent event1 = new ClientCreatedEvent(clientId, createdAt, source, version);
        ClientCreatedEvent event2 = new ClientCreatedEvent(clientId, createdAt, source, version);

        // Then
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenContentsAreDifferent() {
        // Given
        Long clientId1 = 1L;
        Long clientId2 = 2L;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = 1;
        ClientCreatedEvent event1 = new ClientCreatedEvent(clientId1, createdAt, source, version);
        ClientCreatedEvent event2 = new ClientCreatedEvent(clientId2, createdAt, source, version);

        // Then
        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void shouldNotBeEqualWithNull() {
        // Given
        Long clientId = 1L;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = 1;
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, createdAt, source, version);

        // Then
        assertNotEquals(event, null);
    }

    @Test
    void shouldNotBeEqualWithDifferentClass() {
        // Given
        Long clientId = 1L;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = 1;
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, createdAt, source, version);
        Object obj = new Object();

        // Then
        assertNotEquals(event, obj);
    }

    @Test
    void shouldHandleNegativeValues() {
        // Given
        Long clientId = -1L;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = -1;

        // When
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, createdAt, source, version);

        // Then
        assertEquals(-1L, event.clientId());
        assertEquals(-1, event.version());
    }

    @Test
    void shouldHandleLargeValues() {
        // Given
        Long clientId = Long.MAX_VALUE;
        Instant createdAt = Instant.now();
        String source = "service-client";
        int version = Integer.MAX_VALUE;

        // When
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, createdAt, source, version);

        // Then
        assertEquals(Long.MAX_VALUE, event.clientId());
        assertEquals(Integer.MAX_VALUE, event.version());
    }
}
