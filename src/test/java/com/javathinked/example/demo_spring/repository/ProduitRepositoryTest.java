package com.javathinked.example.demo_spring.repository;

import com.javathinked.example.demo_spring.model.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProduitRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProduitRepository produitRepository;

    private Produit testProduit1;
    private Produit testProduit2;

    @BeforeEach
    void setUp() {
        testProduit1 = new Produit();
        testProduit1.setName("Produit A");
        testProduit1.setPrice(10.50f);
        testProduit1.setDescription("Description A");
        testProduit1.setStock(100);
        testProduit1.setColor("Red");
        testProduit1.setImageUrl("http://example.com/a.jpg");

        testProduit2 = new Produit();
        testProduit2.setName("Produit B");
        testProduit2.setPrice(25.75f);
        testProduit2.setDescription("Description B");
        testProduit2.setStock(50);
        testProduit2.setColor("Blue");
        testProduit2.setImageUrl("http://example.com/b.jpg");
    }

    @Test
    void shouldSaveProduit() {
        // When
        Produit savedProduit = produitRepository.save(testProduit1);

        // Then
        assertNotNull(savedProduit.getId());
        assertEquals("Produit A", savedProduit.getName());
        assertEquals(10.50f, savedProduit.getPrice());
        assertEquals("Description A", savedProduit.getDescription());
        assertEquals(100, savedProduit.getStock());
        assertEquals("Red", savedProduit.getColor());
        assertEquals("http://example.com/a.jpg", savedProduit.getImageUrl());
    }

    @Test
    void shouldFindProduitById() {
        // Given
        Produit savedProduit = entityManager.persistAndFlush(testProduit1);

        // When
        Optional<Produit> foundProduit = produitRepository.findById(savedProduit.getId());

        // Then
        assertTrue(foundProduit.isPresent());
        assertEquals(savedProduit.getId(), foundProduit.get().getId());
        assertEquals("Produit A", foundProduit.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenProduitNotFound() {
        // When
        Optional<Produit> foundProduit = produitRepository.findById(999L);

        // Then
        assertFalse(foundProduit.isPresent());
    }

    @Test
    void shouldFindAllProduits() {
        // Given
        entityManager.persistAndFlush(testProduit1);
        entityManager.persistAndFlush(testProduit2);

        // When
        List<Produit> produits = produitRepository.findAll();

        // Then
        assertEquals(2, produits.size());
        assertTrue(produits.stream().anyMatch(p -> "Produit A".equals(p.getName())));
        assertTrue(produits.stream().anyMatch(p -> "Produit B".equals(p.getName())));
    }

    @Test
    void shouldReturnEmptyListWhenNoProduits() {
        // When
        List<Produit> produits = produitRepository.findAll();

        // Then
        assertTrue(produits.isEmpty());
    }

    @Test
    void shouldDeleteProduit() {
        // Given
        Produit savedProduit = entityManager.persistAndFlush(testProduit1);
        Long produitId = savedProduit.getId();

        // When
        produitRepository.deleteById(produitId);

        // Then
        Optional<Produit> deletedProduit = produitRepository.findById(produitId);
        assertFalse(deletedProduit.isPresent());
    }

    @Test
    void shouldCheckIfProduitExists() {
        // Given
        Produit savedProduit = entityManager.persistAndFlush(testProduit1);

        // When
        boolean exists = produitRepository.existsById(savedProduit.getId());
        boolean notExists = produitRepository.existsById(999L);

        // Then
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void shouldUpdateProduit() {
        // Given
        Produit savedProduit = entityManager.persistAndFlush(testProduit1);
        savedProduit.setName("Updated Produit");
        savedProduit.setPrice(15.75f);

        // When
        Produit updatedProduit = produitRepository.save(savedProduit);

        // Then
        assertEquals("Updated Produit", updatedProduit.getName());
        assertEquals(15.75f, updatedProduit.getPrice());
        assertEquals(savedProduit.getId(), updatedProduit.getId());
    }

    @Test
    void shouldHandleNullValues() {
        // Given
        Produit produitWithNulls = new Produit();
        produitWithNulls.setName(null);
        produitWithNulls.setPrice(null);
        produitWithNulls.setDescription(null);
        produitWithNulls.setStock(null);
        produitWithNulls.setColor(null);
        produitWithNulls.setImageUrl(null);

        // When
        Produit savedProduit = produitRepository.save(produitWithNulls);

        // Then
        assertNotNull(savedProduit.getId());
        assertNull(savedProduit.getName());
        assertNull(savedProduit.getPrice());
        assertNull(savedProduit.getDescription());
        assertNull(savedProduit.getStock());
        assertNull(savedProduit.getColor());
        assertNull(savedProduit.getImageUrl());
    }
}
