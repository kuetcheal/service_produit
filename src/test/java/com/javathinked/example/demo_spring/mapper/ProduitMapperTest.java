package com.javathinked.example.demo_spring.mapper;

import com.javathinked.example.demo_spring.dto.ProduitDto;
import com.javathinked.example.demo_spring.model.Produit;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ProduitMapperTest {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // ---------- helpers ----------
    private static ProduitDto dto(Long id, String name, float price, String desc, int stock, String color, String createdAt) {
        ProduitDto d = new ProduitDto();
        d.setId(id);
        d.setName(name);
        d.setPrice(price);
        d.setDescription(desc);
        d.setStock(stock);
        d.setColor(color);
        d.setCreatedAt(createdAt);
        return d;
    }

    private static Produit entity(Long id, String name, float price, String desc, int stock, String color, LocalDateTime createdAt) {
        Produit p = new Produit();
        p.setId(id);
        p.setName(name);
        p.setPrice(price);
        p.setDescription(desc);
        p.setStock(stock);
        p.setColor(color);
        p.setCreatedAt(createdAt);
        return p;
    }

    // ---------- tests DTO -> Entity ----------

    @Test
    void toEntity_nullDto_returnsNull() {
        assertNull(ProduitMapper.toEntity(null));
    }

    @Test
    void toEntity_mapsAllFields() {
        var in = dto(42L, "Stylo", 1.5f, "Bleu", 10, "blue", "IGNORED");
        var out = ProduitMapper.toEntity(in);

        assertNotNull(out);
        // id : ta JPA le régénère, mais le mapper copie le dto.getId() -> on vérifie la copie
        assertEquals(42L, out.getId());
        assertEquals("Stylo", out.getName());
        assertEquals(1.5f, out.getPrice());
        assertEquals("Bleu", out.getDescription());
        assertEquals(10, out.getStock());        
        assertEquals("blue", out.getColor());
        // createdAt est géré en @PrePersist dans l’entité => pas de mapping depuis le DTO
        assertNull(out.getCreatedAt());
    }

    // ---------- tests Entity -> DTO ----------

    @Test
    void toDto_nullEntity_returnsNull() {
        assertNull(ProduitMapper.toDto(null));
    }

    @Test
    void toDto_mapsAllFields_andFormatsCreatedAt() {
        var created = LocalDateTime.of(2025, 8, 24, 9, 30, 15);
        var in = entity(7L, "Cahier", 3.2f, "A5", 5, "red", created);

        var out = ProduitMapper.toDto(in);

        assertNotNull(out);
        assertEquals(7L, out.getId());
        assertEquals("Cahier", out.getName());
        assertEquals(3.2f, out.getPrice());
        assertEquals("A5", out.getDescription());
        assertEquals(5, out.getStock());
        assertEquals("red", out.getColor());
        assertEquals(created.format(ISO), out.getCreatedAt());
    }

    @Test
    void toDto_createdAtNull_leavesDtoCreatedAtNull() {
        var in = entity(1L, "X", 1f, "Y", 1, "Z", null);

        var out = ProduitMapper.toDto(in);

        assertNotNull(out);
        assertNull(out.getCreatedAt());
    }

    // ---------- round-trip ----------

    @Test
    void roundTrip_dtoToEntityToDto_preservesValuesExceptCreatedAt() {
        var original = dto(99L, "Livre", 12.99f, "Roman", 3, "green", "2025-08-24T10:00:00");

        var entity = ProduitMapper.toEntity(original);
        // simulons le @PrePersist qui remplira createdAt en base
        var now = LocalDateTime.of(2025, 8, 24, 11, 0, 0);
        entity.setCreatedAt(now);

        var back = ProduitMapper.toDto(entity);

        assertEquals(99L, back.getId());
        assertEquals("Livre", back.getName());
        assertEquals(12.99f, back.getPrice());
        assertEquals("Roman", back.getDescription());
        assertEquals(3, back.getStock());
        assertEquals("green", back.getColor());
        // createdAt est réécrit à partir de l’entité (format ISO)
        assertEquals(now.format(ISO), back.getCreatedAt());
    }
}
