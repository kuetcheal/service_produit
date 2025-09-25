package com.javathinked.example.demo_spring.dto.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ProductStockUpdatedEventTest {

    @Test
    void shouldCreateProductStockUpdatedEventWithAllFields() {
        // Given
        Long productId = 1L;
        int newStock = 50;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = 1;

        // When
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);

        // Then
        assertNotNull(event);
        assertEquals(productId, event.productId());
        assertEquals(newStock, event.newStock());
        assertEquals(updatedAt, event.updatedAt());
        assertEquals(source, event.source());
        assertEquals(version, event.version());
    }

    @Test
    void shouldCreateProductStockUpdatedEventWithNullFields() {
        // Given
        Long productId = null;
        int newStock = 0;
        Instant updatedAt = null;
        String source = null;
        int version = 0;

        // When
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);

        // Then
        assertNotNull(event);
        assertNull(event.productId());
        assertEquals(0, event.newStock());
        assertNull(event.updatedAt());
        assertNull(event.source());
        assertEquals(0, event.version());
    }

    @Test
    void shouldReturnCorrectToString() {
        // Given
        Long productId = 1L;
        int newStock = 50;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = 1;
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);

        // When
        String toString = event.toString();

        // Then
        assertTrue(toString.contains("productId=1"));
        assertTrue(toString.contains("newStock=50"));
        assertTrue(toString.contains("source=service-produit"));
        assertTrue(toString.contains("version=1"));
    }

    @Test
    void shouldBeEqualWhenContentsAreSame() {
        // Given
        Long productId = 1L;
        int newStock = 50;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = 1;
        ProductStockUpdatedEvent event1 = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);
        ProductStockUpdatedEvent event2 = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);

        // Then
        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenContentsAreDifferent() {
        // Given
        Long productId1 = 1L;
        Long productId2 = 2L;
        int newStock = 50;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = 1;
        ProductStockUpdatedEvent event1 = new ProductStockUpdatedEvent(productId1, newStock, updatedAt, source, version);
        ProductStockUpdatedEvent event2 = new ProductStockUpdatedEvent(productId2, newStock, updatedAt, source, version);

        // Then
        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void shouldNotBeEqualWithNull() {
        // Given
        Long productId = 1L;
        int newStock = 50;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = 1;
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);

        // Then
        assertNotEquals(event, null);
    }

    @Test
    void shouldNotBeEqualWithDifferentClass() {
        // Given
        Long productId = 1L;
        int newStock = 50;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = 1;
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);
        Object obj = new Object();

        // Then
        assertNotEquals(event, obj);
    }

    @Test
    void shouldHandleNegativeValues() {
        // Given
        Long productId = -1L;
        int newStock = -10;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = -1;

        // When
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);

        // Then
        assertEquals(-1L, event.productId());
        assertEquals(-10, event.newStock());
        assertEquals(-1, event.version());
    }

    @Test
    void shouldHandleLargeValues() {
        // Given
        Long productId = Long.MAX_VALUE;
        int newStock = Integer.MAX_VALUE;
        Instant updatedAt = Instant.now();
        String source = "service-produit";
        int version = Integer.MAX_VALUE;

        // When
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent(productId, newStock, updatedAt, source, version);

        // Then
        assertEquals(Long.MAX_VALUE, event.productId());
        assertEquals(Integer.MAX_VALUE, event.newStock());
        assertEquals(Integer.MAX_VALUE, event.version());
    }
}
