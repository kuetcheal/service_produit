package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.dto.events.ProductStockUpdatedEvent;
import com.javathinked.example.demo_spring.messaging.ProductPublisher;
import com.javathinked.example.demo_spring.model.Produit;
import com.javathinked.example.demo_spring.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository repo;
    private final ProductPublisher publisher;

    public ProduitServiceImpl(ProduitRepository repo, ProductPublisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    @Override
    public List<Produit> getAllProduits() {
        return repo.findAll();
    }

    @Override
    public Optional<Produit> getProduitById(Long id) {
        return repo.findById(id);
    }

    @Override
    @Transactional
    public Produit createProduit(Produit produit) {
        return repo.save(produit);
    }

    @Override
    @Transactional
    public Produit updateProduit(Long id, Produit produit) {
        Produit existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + id));
        existing.setName(produit.getName());
        existing.setPrice(produit.getPrice());
        existing.setDescription(produit.getDescription());
        existing.setStock(produit.getStock());
        existing.setColor(produit.getColor());
        return repo.save(existing);
    }

    @Override
    @Transactional
    public void deleteProduit(Long id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public Produit updateStock(Long id, int newStock) {
        Produit p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + id));

        p.setStock(newStock);
        Produit saved = repo.save(p);

        // 👉 publier l’événement après la réussite de la MAJ DB
        var evt = new ProductStockUpdatedEvent(
                saved.getId(),
                saved.getStock(),
                Instant.now(),
                "service-produit",
                1
        );
        publisher.publishStockUpdated(evt);

        return saved;
    }
}
