package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.dto.events.ProductStockUpdatedEvent;
import com.javathinked.example.demo_spring.messaging.ProductPublisher;
import com.javathinked.example.demo_spring.model.Produit;
import com.javathinked.example.demo_spring.repository.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires de la couche service (sans Spring).
 * On isole la logique de ProduitServiceImpl avec des mocks du repository et du publisher.
 */
@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock
    ProduitRepository repo;

    @Mock
    ProductPublisher publisher;

    @InjectMocks
    ProduitServiceImpl service;

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------
    private static Produit produit(Long id, String name, float price, String desc, int stock, String color) {
        Produit p = new Produit();
        p.setId(id);
        p.setName(name);
        p.setPrice(price);
        p.setDescription(desc);
        p.setStock(stock);
        p.setColor(color);
        return p;
    }

    // ----------------------------------------------------
    // findAll
    // ----------------------------------------------------
    @Test
    void getAllProduits_returnsList() {
        when(repo.findAll()).thenReturn(List.of(
                produit(1L, "A", 10f, "a", 5, "red"),
                produit(2L, "B", 12f, "b", 7, "blue")
        ));

        var out = service.getAllProduits();

        assertEquals(2, out.size());
        assertEquals("A", out.get(0).getName());
        verify(repo).findAll();
        verifyNoMoreInteractions(repo, publisher);
    }

    // ----------------------------------------------------
    // findById
    // ----------------------------------------------------
    @Test
    void getProduitById_found() {
        when(repo.findById(9L)).thenReturn(Optional.of(produit(9L, "A", 10f, "a", 5, "red")));

        var opt = service.getProduitById(9L);

        assertTrue(opt.isPresent());
        assertEquals("A", opt.get().getName());
        verify(repo).findById(9L);
        verifyNoMoreInteractions(repo, publisher);
    }

    @Test
    void getProduitById_notFound() {
        when(repo.findById(77L)).thenReturn(Optional.empty());

        var opt = service.getProduitById(77L);

        assertTrue(opt.isEmpty());
        verify(repo).findById(77L);
        verifyNoMoreInteractions(repo, publisher);
    }

    // ----------------------------------------------------
    // create
    // ----------------------------------------------------
    @Test
    void createProduit_savesAndReturnsEntity() {
        var in = produit(null, "Nouveau", 15f, "desc", 3, "black");
        when(repo.save(any(Produit.class))).thenAnswer(inv -> {
            Produit p = inv.getArgument(0);
            p.setId(123L);
            return p;
        });

        var saved = service.createProduit(in);

        assertEquals(123L, saved.getId());
        assertEquals("Nouveau", saved.getName());
        verify(repo).save(any(Produit.class));
        verifyNoMoreInteractions(repo, publisher);
    }

    // ----------------------------------------------------
    // update
    // ----------------------------------------------------
    @Test
    void updateProduit_updatesFields() {
        var existing = produit(5L, "Old", 9f, "old", 1, "white");
        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Produit.class))).thenAnswer(inv -> inv.getArgument(0));

        var changes = produit(null, "New", 11f, "new", 8, "green");
        var out = service.updateProduit(5L, changes);

        assertEquals(5L, out.getId());
        assertEquals("New", out.getName());
        assertEquals(11f, out.getPrice());
        assertEquals("new", out.getDescription());
        assertEquals(8, out.getStock());
        assertEquals("green", out.getColor());

        verify(repo).findById(5L);
        verify(repo).save(any(Produit.class));
        verifyNoMoreInteractions(repo, publisher);
    }

    @Test
    void updateProduit_notFound_throws() {
        when(repo.findById(88L)).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateProduit(88L, produit(null, "X", 1f, "x", 1, "y")));

        assertTrue(ex.getMessage().contains("88"));
        verify(repo).findById(88L);
        verifyNoMoreInteractions(repo, publisher);
    }

    // ----------------------------------------------------
    // delete
    // ----------------------------------------------------
    @Test
    void deleteProduit_callsRepo() {
        service.deleteProduit(10L);
        verify(repo).deleteById(10L);
        verifyNoMoreInteractions(repo, publisher);
    }

    // ----------------------------------------------------
    // updateStock + publication d’événement
    // ----------------------------------------------------
    @Test
    void updateStock_shouldSaveAndPublishEvent() {
        var existing = produit(9L, "Stylo", 1.5f, "desc", 10, "bleu");
        when(repo.findById(9L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Produit.class))).thenAnswer(inv -> inv.getArgument(0));

        var out = service.updateStock(9L, 15);

        // côté persistance
        assertEquals(15, out.getStock());
        verify(repo).findById(9L);
        verify(repo).save(any(Produit.class));

        // côté événement (record ProductStockUpdatedEvent)
        ArgumentCaptor<ProductStockUpdatedEvent> cap =
                ArgumentCaptor.forClass(ProductStockUpdatedEvent.class);
        verify(publisher).publishStockUpdated(cap.capture());

        var evt = cap.getValue();
        assertEquals(9L, evt.productId());
        assertEquals(15, evt.newStock());
        assertNotNull(evt.updatedAt());                        // <- champ correct de ton record
        assertTrue(evt.updatedAt().isBefore(Instant.now().plusSeconds(5)));
        assertEquals("service-produit", evt.source());
        assertEquals(1, evt.version());

        verifyNoMoreInteractions(repo, publisher);
    }

    @Test
    void updateStock_notFound_shouldThrowAndNotPublish() {
        when(repo.findById(77L)).thenReturn(Optional.empty());

        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateStock(77L, 3));

        assertTrue(ex.getMessage().contains("77"));
        verify(repo).findById(77L);
        verifyNoInteractions(publisher);
        verifyNoMoreInteractions(repo);
    }
}
