package com.javathinked.example.demo_spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javathinked.example.demo_spring.model.Produit;
import com.javathinked.example.demo_spring.service.ProduitService;
import com.javathinked.example.demo_spring.security.JwtAuthFilter;
import com.javathinked.example.demo_spring.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires (slice MVC) du ProduitController.
 * - Mock de la couche service
 * - Désactivation de l'exécution des filtres de sécurité
 * - Mock des beans de sécurité pour permettre le chargement du contexte Spring
 */
@WebMvcTest(controllers = ProduitController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProduitControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    // couche service mockée
    @MockBean ProduitService produitService;

    // ➜ mocks des beans sécurité requis par le contexte
    @MockBean JwtAuthFilter jwtAuthFilter;
    @MockBean JwtUtil jwtUtil;

    private Produit produit(Long id, String name, Float price, String description, Integer stock, String color) {
        Produit p = new Produit();
        p.setId(id);
        p.setName(name);
        p.setPrice(price);
        p.setDescription(description);
        p.setStock(stock);
        p.setColor(color);
        return p;
    }

    // ---------- GET /api/produits ----------
    @Test
    void getAll_shouldReturn200_andList() throws Exception {
        Mockito.when(produitService.getAllProduits()).thenReturn(
            List.of(
                produit(1L, "Stylo", 1.2f, "Stylo bleu", 10, "blue"),
                produit(2L, "Cahier", 2.5f, "96 pages", 5, "white")
            )
        );

        mockMvc.perform(get("/api/produits"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Stylo"))
            .andExpect(jsonPath("$[0].price").value(1.2))
            .andExpect(jsonPath("$[0].description").value("Stylo bleu"))
            .andExpect(jsonPath("$[0].stock").value(10))
            .andExpect(jsonPath("$[0].color").value("blue"));
    }

    // ---------- GET /api/produits/{id} ----------
    @Test
    void getById_found_shouldReturn200_andDto() throws Exception {
        Mockito.when(produitService.getProduitById(1L))
            .thenReturn(Optional.of(produit(1L, "Stylo", 1.2f, "Stylo bleu", 10, "blue")));

        mockMvc.perform(get("/api/produits/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Stylo"))
            .andExpect(jsonPath("$.price").value(1.2))
            .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void getById_notFound_shouldReturn404() throws Exception {
        Mockito.when(produitService.getProduitById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produits/999"))
            .andExpect(status().isNotFound());
    }

    // ---------- POST /api/produits ----------
    @Test
    void create_shouldReturn201_andBody() throws Exception {
        String body = """
          {
            "name": "Clavier",
            "price": 19.9,
            "description": "AZERTY",
            "stock": 7,
            "color": "black"
          }
        """;

        var saved = produit(42L, "Clavier", 19.9f, "AZERTY", 7, "black");
        Mockito.when(produitService.createProduit(any(Produit.class))).thenReturn(saved);

        mockMvc.perform(post("/api/produits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/produits/42"))
            .andExpect(jsonPath("$.name").value("Clavier"))
            .andExpect(jsonPath("$.price").value(19.9))
            .andExpect(jsonPath("$.stock").value(7))
            .andExpect(jsonPath("$.color").value("black"));
    }

    // ---------- PUT /api/produits/{id} ----------
    @Test
    void update_shouldReturn200_andUpdatedDto() throws Exception {
        String body = """
          {
            "name": "Stylo bleu",
            "price": 1.3,
            "description": "Pointe fine",
            "stock": 12,
            "color": "blue"
          }
        """;

        var updated = produit(1L, "Stylo bleu", 1.3f, "Pointe fine", 12, "blue");
        Mockito.when(produitService.updateProduit(eq(1L), any(Produit.class))).thenReturn(updated);

        mockMvc.perform(put("/api/produits/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Stylo bleu"))
            .andExpect(jsonPath("$.price").value(1.3))
            .andExpect(jsonPath("$.stock").value(12));
    }

    // ---------- DELETE /api/produits/{id} ----------
    @Test
    void delete_shouldReturn204() throws Exception {
        Mockito.doNothing().when(produitService).deleteProduit(1L);

        mockMvc.perform(delete("/api/produits/1"))
            .andExpect(status().isNoContent());
    }

    // ---------- POST /api/produits/{id}/stock?newStock=... ----------
    @Test
    void updateStock_shouldReturn200_andUpdatedDto() throws Exception {
        Mockito.when(produitService.updateStock(1L, 20))
            .thenReturn(produit(1L, "Stylo", 1.2f, "Stylo bleu", 20, "blue"));

        mockMvc.perform(post("/api/produits/1/stock")
                .param("newStock", "20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.stock").value(20));
    }
}
