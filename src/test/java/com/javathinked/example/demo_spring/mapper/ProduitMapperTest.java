package com.javathinked.example.demo_spring.mapper;

import com.javathinked.example.demo_spring.dto.ProduitDto;
import com.javathinked.example.demo_spring.model.Produit;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProduitMapperTest {

    @Test
    void shouldMapProduitToProduitDto() {
        // Given
        Produit produit = new Produit("Laptop", 1200.0f, "Powerful laptop", 50, "Silver", "url_laptop");
        produit.setId(1L);
        produit.setCreatedAt(LocalDateTime.now());

        // When
        ProduitDto produitDto = ProduitMapper.toDto(produit);

        // Then
        assertNotNull(produitDto);
        assertEquals(produit.getId(), produitDto.getId());
        assertEquals(produit.getName(), produitDto.getName());
        assertEquals(produit.getPrice(), produitDto.getPrice());
        assertEquals(produit.getDescription(), produitDto.getDescription());
        assertEquals(produit.getStock(), produitDto.getStock());
        assertEquals(produit.getColor(), produitDto.getColor());
        assertEquals(produit.getImageUrl(), produitDto.getImageUrl());
        assertNotNull(produitDto.getCreatedAt());
    }

    @Test
    void shouldMapProduitDtoToProduit() {
        // Given
        ProduitDto produitDto = new ProduitDto();
        produitDto.setId(1L);
        produitDto.setName("Laptop");
        produitDto.setPrice(1200.0f);
        produitDto.setDescription("Powerful laptop");
        produitDto.setStock(50);
        produitDto.setColor("Silver");
        produitDto.setImageUrl("url_laptop");

        // When
        Produit produit = ProduitMapper.toEntity(produitDto);

        // Then
        assertNotNull(produit);
        assertEquals(produitDto.getId(), produit.getId());
        assertEquals(produitDto.getName(), produit.getName());
        assertEquals(produitDto.getPrice(), produit.getPrice());
        assertEquals(produitDto.getDescription(), produit.getDescription());
        assertEquals(produitDto.getStock(), produit.getStock());
        assertEquals(produitDto.getColor(), produit.getColor());
        assertEquals(produitDto.getImageUrl(), produit.getImageUrl());
    }

    @Test
    void shouldHandleNullProduitWhenMappingToDto() {
        // When
        ProduitDto produitDto = ProduitMapper.toDto(null);

        // Then
        assertNull(produitDto);
    }

    @Test
    void shouldHandleNullProduitDtoWhenMappingToEntity() {
        // When
        Produit produit = ProduitMapper.toEntity(null);

        // Then
        assertNull(produit);
    }

    @Test
    void shouldHandleProduitWithNullFieldsToDto() {
        // Given
        Produit produit = new Produit();
        produit.setId(1L); // ID can be set even if other fields are null

        // When
        ProduitDto produitDto = ProduitMapper.toDto(produit);

        // Then
        assertNotNull(produitDto);
        assertEquals(1L, produitDto.getId());
        assertNull(produitDto.getName());
        assertNull(produitDto.getPrice());
        assertNull(produitDto.getDescription());
        assertNull(produitDto.getStock());
        assertNull(produitDto.getColor());
        assertNull(produitDto.getImageUrl());
        assertNull(produitDto.getCreatedAt());
    }

    @Test
    void shouldHandleProduitDtoWithNullFieldsToEntity() {
        // Given
        ProduitDto produitDto = new ProduitDto();
        produitDto.setId(1L);

        // When
        Produit produit = ProduitMapper.toEntity(produitDto);

        // Then
        assertNotNull(produit);
        assertEquals(1L, produit.getId());
        assertNull(produit.getName());
        assertNull(produit.getPrice());
        assertNull(produit.getDescription());
        assertNull(produit.getStock());
        assertNull(produit.getColor());
        assertNull(produit.getImageUrl());
    }

    @Test
    void shouldHandleProduitWithNullCreatedAtToDto() {
        // Given
        Produit produit = new Produit("Test", 10.0f, "Description", 1, "Red", "image.jpg");
        produit.setId(1L);
        produit.setCreatedAt(null);

        // When
        ProduitDto produitDto = ProduitMapper.toDto(produit);

        // Then
        assertNotNull(produitDto);
        assertEquals(1L, produitDto.getId());
        assertEquals("Test", produitDto.getName());
        assertNull(produitDto.getCreatedAt());
    }
}
