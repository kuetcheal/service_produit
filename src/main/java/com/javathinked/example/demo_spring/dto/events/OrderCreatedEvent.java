package com.javathinked.example.demo_spring.dto.events;

import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
    Long orderId,
    Long customerId,
    List<Long> productIds,
    Instant createdAt,   // optionnel
    String source,       // "service-commande"
    int version          // 1
) {}
