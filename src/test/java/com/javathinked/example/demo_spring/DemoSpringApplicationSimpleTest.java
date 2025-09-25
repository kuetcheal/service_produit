package com.javathinked.example.demo_spring;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DemoSpringApplicationSimpleTest {

    @Test
    void shouldCreateApplication() {
        // Given & When
        DemoSpringApplication application = new DemoSpringApplication();

        // Then
        assertNotNull(application);
    }

    @Test
    void shouldHaveMainMethod() {
        // Given
        String[] args = {"test"};

        // When & Then
        // On teste que la méthode main existe et peut être appelée
        assertDoesNotThrow(() -> {
            // On ne peut pas vraiment tester main() sans lancer l'application complète
            // mais on peut vérifier que la classe est bien formée
            DemoSpringApplication.class.getDeclaredMethod("main", String[].class);
        });
    }
}
