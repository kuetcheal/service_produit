package com.javathinked.example.demo_spring.dto.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderCreatedEventTest {

    @Test
    void shouldCreateOrderCreatedEventWithAllFields() {
        // Given
        Long orderId = 1L;
        Long customerId = 2L;
        List<Long> productIds = Arrays.asList(10L, 20L, 30L);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = 1;

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.orderId());
        assertEquals(customerId, event.customerId());
        assertEquals(productIds, event.productIds());
        assertEquals(createdAt, event.createdAt());
        assertEquals(source, event.source());
        assertEquals(version, event.version());
    }

    @Test
    void shouldCreateOrderCreatedEventWithNullFields() {
        // Given
        Long orderId = null;
        Long customerId = null;
        List<Long> productIds = null;
        Instant createdAt = null;
        String source = null;
        int version = 0;

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // Then
        assertNotNull(event);
        assertNull(event.orderId());
        assertNull(event.customerId());
        assertNull(event.productIds());
        assertNull(event.createdAt());
        assertNull(event.source());
        assertEquals(0, event.version());
    }

    @Test
    void shouldCreateOrderCreatedEventWithEmptyProductIds() {
        // Given
        Long orderId = 1L;
        Long customerId = 2L;
        List<Long> productIds = Arrays.asList();
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = 1;

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.orderId());
        assertEquals(customerId, event.customerId());
        assertTrue(event.productIds().isEmpty());
        assertEquals(createdAt, event.createdAt());
        assertEquals(source, event.source());
        assertEquals(version, event.version());
    }

    @Test
    void shouldReturnCorrectToString() {
        // Given
        Long orderId = 1L;
        Long customerId = 2L;
        List<Long> productIds = Arrays.asList(10L, 20L);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = 1;
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // When
        String toString = event.toString();

        // Then
        assertTrue(toString.contains("orderId=1"));
        assertTrue(toString.contains("customerId=2"));
        assertTrue(toString.contains("source=service-commande"));
        assertTrue(toString.contains("version=1"));
    }

    @Test
    void shouldBeEqualWhenContentsAreSame() {
        // Given
        Long orderId = 1L;
        Long customerId = 2L;
        List<Long> productIds = Arrays.asList(10L, 20L);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = 1;
        OrderCreatedEvent event1 = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);
        OrderCreatedEvent event2 = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // Then
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenContentsAreDifferent() {
        // Given
        Long orderId1 = 1L;
        Long orderId2 = 2L;
        Long customerId = 2L;
        List<Long> productIds = Arrays.asList(10L, 20L);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = 1;
        OrderCreatedEvent event1 = new OrderCreatedEvent(orderId1, customerId, productIds, createdAt, source, version);
        OrderCreatedEvent event2 = new OrderCreatedEvent(orderId2, customerId, productIds, createdAt, source, version);

        // Then
        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void shouldNotBeEqualWithNull() {
        // Given
        Long orderId = 1L;
        Long customerId = 2L;
        List<Long> productIds = Arrays.asList(10L, 20L);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = 1;
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // Then
        assertNotEquals(event, null);
    }

    @Test
    void shouldNotBeEqualWithDifferentClass() {
        // Given
        Long orderId = 1L;
        Long customerId = 2L;
        List<Long> productIds = Arrays.asList(10L, 20L);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = 1;
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);
        Object obj = new Object();

        // Then
        assertNotEquals(event, obj);
    }

    @Test
    void shouldHandleNegativeValues() {
        // Given
        Long orderId = -1L;
        Long customerId = -2L;
        List<Long> productIds = Arrays.asList(-10L, -20L);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = -1;

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // Then
        assertEquals(-1L, event.orderId());
        assertEquals(-2L, event.customerId());
        assertEquals(Arrays.asList(-10L, -20L), event.productIds());
        assertEquals(-1, event.version());
    }

    @Test
    void shouldHandleLargeValues() {
        // Given
        Long orderId = Long.MAX_VALUE;
        Long customerId = Long.MAX_VALUE;
        List<Long> productIds = Arrays.asList(Long.MAX_VALUE, Long.MAX_VALUE);
        Instant createdAt = Instant.now();
        String source = "service-commande";
        int version = Integer.MAX_VALUE;

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, customerId, productIds, createdAt, source, version);

        // Then
        assertEquals(Long.MAX_VALUE, event.orderId());
        assertEquals(Long.MAX_VALUE, event.customerId());
        assertEquals(Arrays.asList(Long.MAX_VALUE, Long.MAX_VALUE), event.productIds());
        assertEquals(Integer.MAX_VALUE, event.version());
    }
}
