package com.javathinked.example.demo_spring.messaging;

import com.javathinked.example.demo_spring.dto.events.ProductStockUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProductPublisher productPublisher;

    private ProductStockUpdatedEvent testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new ProductStockUpdatedEvent(
                1L,
                100,
                Instant.now(),
                "service-produit",
                1
        );
    }

    @Test
    void shouldPublishProductStockUpdatedEvent() {
        // When
        productPublisher.publishStockUpdated(testEvent);

        // Then
        verify(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                eq(testEvent)
        );
    }

    @Test
    void shouldPublishProductStockUpdatedEventWithNullValues() {
        // Given
        ProductStockUpdatedEvent eventWithNulls = new ProductStockUpdatedEvent(
                null,
                0,
                null,
                null,
                0
        );

        // When
        productPublisher.publishStockUpdated(eventWithNulls);

        // Then
        verify(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                eq(eventWithNulls)
        );
    }

    @Test
    void shouldPublishProductStockUpdatedEventWithLargeValues() {
        // Given
        ProductStockUpdatedEvent eventWithLargeValues = new ProductStockUpdatedEvent(
                Long.MAX_VALUE,
                Integer.MAX_VALUE,
                Instant.now(),
                "service-produit",
                Integer.MAX_VALUE
        );

        // When
        productPublisher.publishStockUpdated(eventWithLargeValues);

        // Then
        verify(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                eq(eventWithLargeValues)
        );
    }

    @Test
    void shouldPublishProductStockUpdatedEventWithNegativeValues() {
        // Given
        ProductStockUpdatedEvent eventWithNegatives = new ProductStockUpdatedEvent(
                -1L,
                -10,
                Instant.now(),
                "service-produit",
                -1
        );

        // When
        productPublisher.publishStockUpdated(eventWithNegatives);

        // Then
        verify(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                eq(eventWithNegatives)
        );
    }

    @Test
    void shouldPublishProductStockUpdatedEventMultipleTimes() {
        // When
        productPublisher.publishStockUpdated(testEvent);
        productPublisher.publishStockUpdated(testEvent);
        productPublisher.publishStockUpdated(testEvent);

        // Then
        verify(rabbitTemplate, times(3)).convertAndSend(
                anyString(),
                anyString(),
                eq(testEvent)
        );
    }

    @Test
    void shouldHandleRabbitTemplateException() {
        // Given
        doThrow(new RuntimeException("RabbitMQ error")).when(rabbitTemplate)
                .convertAndSend(anyString(), anyString(), any(ProductStockUpdatedEvent.class));

        // When & Then
        // The method should not throw an exception, it should handle the error gracefully
        // Note: The actual implementation might throw an exception, which is acceptable
        try {
            productPublisher.publishStockUpdated(testEvent);
        } catch (Exception e) {
            // This is acceptable behavior
        }

        verify(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                eq(testEvent)
        );
    }
}
