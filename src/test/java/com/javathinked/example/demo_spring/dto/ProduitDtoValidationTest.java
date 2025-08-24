package com.javathinked.example.demo_spring.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProduitDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    private Set<ConstraintViolation<ProduitDto>> validate(ProduitDto dto) {
        return validator.validate(dto);
    }

    private boolean hasViolationOn(Set<ConstraintViolation<ProduitDto>> violations, String property) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(property));
    }

    private ProduitDto valid() {
        ProduitDto dto = new ProduitDto();
        dto.setId(1L);
        dto.setName("Stylo");
        dto.setPrice(1.2f);
        dto.setDescription("Stylo bleu");
        dto.setStock(10);
        dto.setColor("blue");
        dto.setCreatedAt("2025-08-23T08:00:00"); 
        return dto;
    }

    @Test
    void validDto_shouldHaveNoViolations() {
        var violations = validate(valid());
        assertTrue(violations.isEmpty(), "Aucune violation attendue pour un DTO valide");
    }

    @Test
    void name_blank_shouldFail() {
        var dto = valid();
        dto.setName("   "); // @NotBlank
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "name"));
    }

    @Test
    void name_null_shouldFail() {
        var dto = valid();
        dto.setName(null); // @NotBlank
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "name"));
    }

    @Test
    void price_null_shouldFail() {
        var dto = valid();
        dto.setPrice(null); // @NotNull
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "price"));
    }

    @Test
    void price_negative_shouldFail() {
        var dto = valid();
        dto.setPrice(-0.01f); // @PositiveOrZero
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "price"));
    }

    @Test
    void stock_null_shouldFail() {
        var dto = valid();
        dto.setStock(null); // @NotNull
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "stock"));
    }

    @Test
    void stock_negative_shouldFail() {
        var dto = valid();
        dto.setStock(-1); // @PositiveOrZero
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "stock"));
    }

    @Test
    void description_tooLong_shouldFail() {
        var dto = valid();
        dto.setDescription("x".repeat(256)); // @Size(max=255)
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "description"));
    }

    @Test
    void color_tooLong_shouldFail() {
        var dto = valid();
        dto.setColor("x".repeat(51)); // @Size(max=50)
        var violations = validate(dto);
        assertTrue(hasViolationOn(violations, "color"));
    }

    @Test
    void createdAt_anyValue_shouldNotAddViolations() {
        var dto = valid();
        dto.setCreatedAt("not-iso-but-no-constraint");
        var violations = validate(dto);
        // createdAt n'a aucune contrainte → toujours 0 violation si les autres champs sont valides
        assertTrue(violations.isEmpty());
    }
}
