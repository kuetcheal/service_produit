package com.javathinked.example.demo_spring.repository;

import com.javathinked.example.demo_spring.model.Produit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests JPA pour ProduitRepository avec base H2 embarquée.
 */
@DataJpaTest
// Laisse Spring utiliser sa DB embarquée (H2). Si tu veux forcer H2 même si une autre est configurée, laisse cette annotation telle quelle.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ProduitRepositoryTest {

    @Autowired
    ProduitRepository produitRepository;

    @Autowired
    TestEntityManager em; // pour flush/clear et forcer la lecture depuis la DB

    private Produit produit(String name, Float price, String description, Integer stock, String color) {
        Produit p = new Produit();
        p.setName(name);
        p.setPrice(price);
        p.setDescription(description);
        p.setStock(stock);
        p.setColor(color);
        return p;
    }

    @Test
    void save_and_findById_shouldPersistAndRetrieve() {
        // arrange
        Produit p = produit("Stylo", 1.2f, "Bleu", 10, "blue");

        // act
        Produit saved = produitRepository.save(p);
        em.flush();
        em.clear(); // on évite le cache de persistance

        // assert
        assertNotNull(saved.getId(), "id doit être généré");
        Optional<Produit> fromDb = produitRepository.findById(saved.getId());
        assertTrue(fromDb.isPresent(), "le produit doit être retrouvé");
        assertNotNull(fromDb.get().getCreatedAt(), "@PrePersist doit avoir rempli createdAt");
        assertEquals("Stylo", fromDb.get().getName());
        assertEquals(10, fromDb.get().getStock());
    }

    @Test
    void findAll_shouldReturnMultiple() {
        produitRepository.saveAll(List.of(
                produit("Stylo", 1.2f, "Bleu", 10, "blue"),
                produit("Cahier", 2.5f, "96 pages", 5, "white")
        ));
        em.flush();
        em.clear();

        List<Produit> all = produitRepository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(p -> "Stylo".equals(p.getName())));
        assertTrue(all.stream().anyMatch(p -> "Cahier".equals(p.getName())));
    }

    @Test
    void delete_shouldRemoveEntity() {
        Produit saved = produitRepository.save(produit("Gomme", 0.8f, "Petite", 20, "white"));
        Long id = saved.getId();
        em.flush();
        em.clear();

        produitRepository.deleteById(id);
        em.flush();
        em.clear();

        assertFalse(produitRepository.findById(id).isPresent(), "l'entité doit être supprimée");
    }

    @Test
    void update_shouldModifyFields() {
        Produit saved = produitRepository.save(produit("Feutre", 1.9f, "Noir", 8, "black"));
        Long id = saved.getId();

        // modification
        saved.setPrice(2.1f);
        saved.setStock(12);
        produitRepository.save(saved);
        em.flush();
        em.clear();

        Produit reloaded = produitRepository.findById(id).orElseThrow();
        assertEquals(2.1f, reloaded.getPrice());
        assertEquals(12, reloaded.getStock());
    }
}
