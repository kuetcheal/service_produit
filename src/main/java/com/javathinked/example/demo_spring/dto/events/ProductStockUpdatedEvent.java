package com.javathinked.example.demo_spring.dto.events;

import java.time.Instant;

public record ProductStockUpdatedEvent(
    Long productId,
    int newStock,
    Instant updatedAt,   // optionnel
    String source,       // "service-produit"
    int version          // 1
) {}
