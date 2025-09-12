package com.javathinked.example.demo_spring.controller;

import com.javathinked.example.demo_spring.dto.ProduitDto;
import com.javathinked.example.demo_spring.mapper.ProduitMapper;
import com.javathinked.example.demo_spring.model.Produit;
import com.javathinked.example.demo_spring.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    // Dossier d'upload (configurable via application.properties)
    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

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

    @PostMapping("/{id}/stock")
    public ResponseEntity<ProduitDto> updateStock(@PathVariable Long id, @RequestParam int newStock) {
        var updated = produitService.updateStock(id, newStock);
        return ResponseEntity.ok(ProduitMapper.toDto(updated));
    }

    // ✅ Nouvel endpoint: upload d'image (multipart) et mise à jour de imageUrl en BDD
    @PostMapping("/{id}/image")
    public ResponseEntity<ProduitDto> uploadImage(@PathVariable Long id,
                                                  @RequestPart("file") MultipartFile file) {
        var opt = produitService.getProduitById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        if (file == null || file.isEmpty()) return ResponseEntity.badRequest().build();

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            original = Paths.get(original).getFileName().toString();
            // petite sanitation
            original = original.replaceAll("[^a-zA-Z0-9._-]", "_");

            String filename = "prod_" + id + "_" + System.currentTimeMillis() + "_" + original;
            Path target = dir.resolve(filename);
            file.transferTo(target.toFile());

            String publicPath = "/uploads/" + filename; // servi par StaticResourceConfig

            Produit produit = opt.get();
            produit.setImageUrl(publicPath);

            Produit saved = produitService.updateProduit(id, produit);
            return ResponseEntity.ok(ProduitMapper.toDto(saved));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
