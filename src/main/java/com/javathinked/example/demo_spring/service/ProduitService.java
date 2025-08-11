package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.model.Produit;

import java.util.List;
import java.util.Optional;

public interface ProduitService {
    List<Produit> getAllProduits();
    Optional<Produit> getProduitById(Long id);
    Produit createProduit(Produit produit);
    Produit updateProduit(Long id, Produit produit);
    void deleteProduit(Long id);

    // ➕ pour publier un event après MAJ stock
    Produit updateStock(Long id, int newStock);
}
