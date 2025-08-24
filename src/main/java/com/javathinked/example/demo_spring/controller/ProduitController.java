package com.javathinked.example.demo_spring.controller;

import com.javathinked.example.demo_spring.dto.ProduitDto;
import com.javathinked.example.demo_spring.mapper.ProduitMapper;
import com.javathinked.example.demo_spring.model.Produit;
import com.javathinked.example.demo_spring.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<ProduitDto> getAll() {
        return produitService.getAllProduits().stream()
                .map(ProduitMapper::toDto)
                .collect(toList());  
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDto> getById(@PathVariable Long id) {
        return produitService.getProduitById(id)
                .map(ProduitMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProduitDto> create(@Valid @RequestBody ProduitDto dto) {
        Produit toSave = ProduitMapper.toEntity(dto);
        Produit saved = produitService.createProduit(toSave);
        ProduitDto out = ProduitMapper.toDto(saved);
        return ResponseEntity.created(URI.create("/api/produits/" + saved.getId()))
                .body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDto> update(@PathVariable Long id, @Valid @RequestBody ProduitDto dto) {
        Produit updated = produitService.updateProduit(id, ProduitMapper.toEntity(dto));
        return ResponseEntity.ok(ProduitMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    // 🔧 Test/usage: met à jour le stock ET publie l'event RabbitMQ
    // POST http://localhost:8081/api/produits/1/stock?newStock=15
    @PostMapping("/{id}/stock")
    public ResponseEntity<ProduitDto> updateStock(@PathVariable Long id, @RequestParam int newStock) {
        var updated = produitService.updateStock(id, newStock);
        return ResponseEntity.ok(ProduitMapper.toDto(updated));
    }
}
