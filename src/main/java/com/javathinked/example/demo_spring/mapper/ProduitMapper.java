package com.javathinked.example.demo_spring.mapper;

import com.javathinked.example.demo_spring.dto.ProduitDto;
import com.javathinked.example.demo_spring.model.Produit;

import java.time.format.DateTimeFormatter;

public final class ProduitMapper {

    private ProduitMapper() {} // util class

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static Produit toEntity(ProduitDto dto) {
        if (dto == null) return null;
        Produit p = new Produit();
        p.setId(dto.getId()); // ignoré en création, utile pour update
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setDescription(dto.getDescription());
        p.setStock(dto.getStock());
        p.setColor(dto.getColor());
        p.setImageUrl(dto.getImageUrl()); // image URL
        // createdAt géré par @PrePersist
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
        dto.setImageUrl(p.getImageUrl());
        dto.setCreatedAt(p.getCreatedAt() == null ? null : p.getCreatedAt().format(ISO));
        return dto;
    }
}
