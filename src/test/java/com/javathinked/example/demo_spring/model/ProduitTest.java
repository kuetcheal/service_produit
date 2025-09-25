package com.javathinked.example.demo_spring.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProduitTest {

    private Produit produit;

    @BeforeEach
    void setUp() {
        produit = new Produit();
    }

    @Test
    void shouldCreateProduitWithDefaultConstructor() {
        // When
        Produit newProduit = new Produit();

        // Then
        assertNotNull(newProduit);
        assertNull(newProduit.getId());
        assertNull(newProduit.getName());
        assertNull(newProduit.getPrice());
        assertNull(newProduit.getDescription());
        assertNull(newProduit.getStock());
        assertNull(newProduit.getColor());
        assertNull(newProduit.getImageUrl());
        assertNull(newProduit.getCreatedAt());
    }

    @Test
    void shouldCreateProduitWithAllParameters() {
        // Given
        String name = "Test Product";
        Float price = 99.99f;
        String description = "Test Description";
        Integer stock = 10;
        String color = "Red";
        String imageUrl = "http://example.com/image.jpg";

        // When
        Produit newProduit = new Produit(name, price, description, stock, color, imageUrl);

        // Then
        assertNotNull(newProduit);
        assertEquals(name, newProduit.getName());
        assertEquals(price, newProduit.getPrice());
        assertEquals(description, newProduit.getDescription());
        assertEquals(stock, newProduit.getStock());
        assertEquals(color, newProduit.getColor());
        assertEquals(imageUrl, newProduit.getImageUrl());
    }

    @Test
    void shouldSetAndGetId() {
        // Given
        Long id = 1L;

        // When
        produit.setId(id);

        // Then
        assertEquals(id, produit.getId());
    }

    @Test
    void shouldSetAndGetName() {
        // Given
        String name = "Test Product";

        // When
        produit.setName(name);

        // Then
        assertEquals(name, produit.getName());
    }

    @Test
    void shouldSetAndGetPrice() {
        // Given
        Float price = 99.99f;

        // When
        produit.setPrice(price);

        // Then
        assertEquals(price, produit.getPrice());
    }

    @Test
    void shouldSetAndGetDescription() {
        // Given
        String description = "Test Description";

        // When
        produit.setDescription(description);

        // Then
        assertEquals(description, produit.getDescription());
    }

    @Test
    void shouldSetAndGetStock() {
        // Given
        Integer stock = 10;

        // When
        produit.setStock(stock);

        // Then
        assertEquals(stock, produit.getStock());
    }

    @Test
    void shouldSetAndGetColor() {
        // Given
        String color = "Red";

        // When
        produit.setColor(color);

        // Then
        assertEquals(color, produit.getColor());
    }

    @Test
    void shouldSetAndGetImageUrl() {
        // Given
        String imageUrl = "http://example.com/image.jpg";

        // When
        produit.setImageUrl(imageUrl);

        // Then
        assertEquals(imageUrl, produit.getImageUrl());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        produit.setCreatedAt(now);

        // Then
        assertEquals(now, produit.getCreatedAt());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        produit.setId(null);
        produit.setName(null);
        produit.setPrice(null);
        produit.setDescription(null);
        produit.setStock(null);
        produit.setColor(null);
        produit.setImageUrl(null);
        produit.setCreatedAt(null);

        // Then
        assertNull(produit.getId());
        assertNull(produit.getName());
        assertNull(produit.getPrice());
        assertNull(produit.getDescription());
        assertNull(produit.getStock());
        assertNull(produit.getColor());
        assertNull(produit.getImageUrl());
        assertNull(produit.getCreatedAt());
    }

    @Test
    void shouldHandleZeroValues() {
        // Given
        Float zeroPrice = 0.0f;
        Integer zeroStock = 0;

        // When
        produit.setPrice(zeroPrice);
        produit.setStock(zeroStock);

        // Then
        assertEquals(0.0f, produit.getPrice());
        assertEquals(0, produit.getStock());
    }

    @Test
    void shouldHandleNegativeValues() {
        // Given
        Float negativePrice = -10.0f;
        Integer negativeStock = -1;

        // When
        produit.setPrice(negativePrice);
        produit.setStock(negativeStock);

        // Then
        assertEquals(-10.0f, produit.getPrice());
        assertEquals(-1, produit.getStock());
    }

    @Test
    void shouldHandleLargeValues() {
        // Given
        Long largeId = Long.MAX_VALUE;
        Float largePrice = Float.MAX_VALUE;
        Integer largeStock = Integer.MAX_VALUE;

        // When
        produit.setId(largeId);
        produit.setPrice(largePrice);
        produit.setStock(largeStock);

        // Then
        assertEquals(Long.MAX_VALUE, produit.getId());
        assertEquals(Float.MAX_VALUE, produit.getPrice());
        assertEquals(Integer.MAX_VALUE, produit.getStock());
    }

    @Test
    void shouldHandleEmptyStrings() {
        // Given
        String emptyName = "";
        String emptyDescription = "";
        String emptyColor = "";
        String emptyImageUrl = "";

        // When
        produit.setName(emptyName);
        produit.setDescription(emptyDescription);
        produit.setColor(emptyColor);
        produit.setImageUrl(emptyImageUrl);

        // Then
        assertEquals("", produit.getName());
        assertEquals("", produit.getDescription());
        assertEquals("", produit.getColor());
        assertEquals("", produit.getImageUrl());
    }

    @Test
    void shouldHandleLongStrings() {
        // Given
        String longName = "Very Long Product Name That Exceeds Normal Length";
        String longDescription = "Very Long Description That Exceeds Normal Length And Contains Many Characters";
        String longColor = "Very Long Color Name";
        String longImageUrl = "http://example.com/very/long/path/to/image.jpg";

        // When
        produit.setName(longName);
        produit.setDescription(longDescription);
        produit.setColor(longColor);
        produit.setImageUrl(longImageUrl);

        // Then
        assertEquals(longName, produit.getName());
        assertEquals(longDescription, produit.getDescription());
        assertEquals(longColor, produit.getColor());
        assertEquals(longImageUrl, produit.getImageUrl());
    }
}
