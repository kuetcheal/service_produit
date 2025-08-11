package com.javathinked.example.demo_spring.dto.events;

import java.time.Instant;

public record ClientCreatedEvent(
    Long clientId,
    Instant createdAt,      // optionnel
    String source,          // "service-client"
    int version             // 1
) {}
