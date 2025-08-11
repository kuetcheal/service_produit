package com.javathinked.example.demo_spring.repository;

import com.javathinked.example.demo_spring.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
