package com.javathinked.example.demo_spring.mapper;

import com.javathinked.example.demo_spring.dto.ProduitDto;
import com.javathinked.example.demo_spring.model.Produit;

import java.time.format.DateTimeFormatter;

public class ProduitMapper {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static Produit toEntity(ProduitDto dto) {
        if (dto == null) return null;
        Produit p = new Produit();
        p.setId(dto.getId()); // géré automatiquement en création
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setDescription(dto.getDescription());
        p.setStock(dto.getStock());
        p.setColor(dto.getColor());
        // createdAt géré automatiquement avec @PrePersist
        return p;
    }

    public static ProduitDto toDto(Produit p) {
        if (p == null) return null;
        ProduitDto dto = new ProduitDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setPrice(p.getPrice());
        dto.setDescription(p.getDescription());
        dto.setStock(p.getStock());
        dto.setColor(p.getColor());
        dto.setCreatedAt(p.getCreatedAt() == null ? null : p.getCreatedAt().format(ISO));
        return dto;
    }
}
