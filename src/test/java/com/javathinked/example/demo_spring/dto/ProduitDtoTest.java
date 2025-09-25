package com.javathinked.example.demo_spring.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProduitDtoTest {

    private ProduitDto produitDto;

    @BeforeEach
    void setUp() {
        produitDto = new ProduitDto();
    }

    @Test
    void shouldCreateProduitDtoWithDefaultConstructor() {
        // Given
        ProduitDto newDto = new ProduitDto();

        // Then
        assertNotNull(newDto);
        assertNull(newDto.getId());
        assertNull(newDto.getName());
        assertNull(newDto.getPrice());
        assertNull(newDto.getDescription());
        assertNull(newDto.getStock());
        assertNull(newDto.getColor());
        assertNull(newDto.getImageUrl());
    }

    @Test
    void shouldSetAndGetId() {
        // Given
        Long id = 1L;

        // When
        produitDto.setId(id);

        // Then
        assertEquals(id, produitDto.getId());
    }

    @Test
    void shouldSetAndGetName() {
        // Given
        String name = "Test Produit";

        // When
        produitDto.setName(name);

        // Then
        assertEquals(name, produitDto.getName());
    }

    @Test
    void shouldSetAndGetPrice() {
        // Given
        Float price = 10.50f;

        // When
        produitDto.setPrice(price);

        // Then
        assertEquals(price, produitDto.getPrice());
    }

    @Test
    void shouldSetAndGetDescription() {
        // Given
        String description = "Description du produit";

        // When
        produitDto.setDescription(description);

        // Then
        assertEquals(description, produitDto.getDescription());
    }

    @Test
    void shouldSetAndGetStock() {
        // Given
        Integer stock = 100;

        // When
        produitDto.setStock(stock);

        // Then
        assertEquals(stock, produitDto.getStock());
    }

    @Test
    void shouldSetAndGetColor() {
        // Given
        String color = "Red";

        // When
        produitDto.setColor(color);

        // Then
        assertEquals(color, produitDto.getColor());
    }

    @Test
    void shouldSetAndGetImageUrl() {
        // Given
        String imageUrl = "http://example.com/image.jpg";

        // When
        produitDto.setImageUrl(imageUrl);

        // Then
        assertEquals(imageUrl, produitDto.getImageUrl());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        produitDto.setId(null);
        produitDto.setName(null);
        produitDto.setPrice(null);
        produitDto.setDescription(null);
        produitDto.setStock(null);
        produitDto.setColor(null);
        produitDto.setImageUrl(null);

        // Then
        assertNull(produitDto.getId());
        assertNull(produitDto.getName());
        assertNull(produitDto.getPrice());
        assertNull(produitDto.getDescription());
        assertNull(produitDto.getStock());
        assertNull(produitDto.getColor());
        assertNull(produitDto.getImageUrl());
    }

    @Test
    void shouldHandleEmptyStrings() {
        // When
        produitDto.setName("");
        produitDto.setDescription("");
        produitDto.setColor("");
        produitDto.setImageUrl("");

        // Then
        assertEquals("", produitDto.getName());
        assertEquals("", produitDto.getDescription());
        assertEquals("", produitDto.getColor());
        assertEquals("", produitDto.getImageUrl());
    }

    @Test
    void shouldHandleZeroValues() {
        // When
        produitDto.setPrice(0.0f);
        produitDto.setStock(0);

        // Then
        assertEquals(0.0f, produitDto.getPrice());
        assertEquals(0, produitDto.getStock());
    }

    @Test
    void shouldHandleLargeValues() {
        // When
        produitDto.setId(Long.MAX_VALUE);
        produitDto.setPrice(Float.MAX_VALUE);
        produitDto.setStock(Integer.MAX_VALUE);

        // Then
        assertEquals(Long.MAX_VALUE, produitDto.getId());
        assertEquals(Float.MAX_VALUE, produitDto.getPrice());
        assertEquals(Integer.MAX_VALUE, produitDto.getStock());
    }

    @Test
    void shouldHandleNegativeValues() {
        // When
        produitDto.setPrice(-10.0f);
        produitDto.setStock(-5);

        // Then
        assertEquals(-10.0f, produitDto.getPrice());
        assertEquals(-5, produitDto.getStock());
    }
}
