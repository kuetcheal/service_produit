package com.javathinked.example.demo_spring.controller;

import com.javathinked.example.demo_spring.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProduitControllerSimpleTest {

    @Mock
    private ProduitService produitService;

    @InjectMocks
    private ProduitController produitController;

    @Test
    void shouldCreateController() {
        // Given & When
        ProduitController controller = new ProduitController(produitService);

        // Then
        assertNotNull(controller);
    }

    @Test
    void shouldHaveProduitService() {
        // Given & When
        ProduitController controller = new ProduitController(produitService);

        // Then
        assertNotNull(controller);
        // Le service est injecté via le constructeur
    }
}
