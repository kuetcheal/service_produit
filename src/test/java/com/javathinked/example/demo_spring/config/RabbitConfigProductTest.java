package com.javathinked.example.demo_spring.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitConfigProductTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitConfigProduct rabbitConfigProduct;

    @Test
    void shouldCreateAppExchange() {
        // When
        TopicExchange exchange = rabbitConfigProduct.appExchange();

        // Then
        assertNotNull(exchange);
        assertEquals("app.exchange", exchange.getName());
        assertTrue(exchange.isDurable());
        assertFalse(exchange.isAutoDelete());
    }

    @Test
    void shouldCreateQProductFromOrder() {
        // When
        Queue queue = rabbitConfigProduct.qFromOrder();

        // Then
        assertNotNull(queue);
        assertEquals("q.product.from-order", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void shouldCreateBindOrder() {
        // Given
        Queue queue = rabbitConfigProduct.qFromOrder();
        TopicExchange exchange = rabbitConfigProduct.appExchange();

        // When
        Binding binding = rabbitConfigProduct.bindOrder(queue, exchange);

        // Then
        assertNotNull(binding);
        assertEquals("q.product.from-order", binding.getDestination());
        assertEquals("app.exchange", binding.getExchange());
        assertEquals("order.*", binding.getRoutingKey());
    }

    @Test
    void shouldCreateJsonConverter() {
        // When
        org.springframework.amqp.support.converter.MessageConverter converter = 
                rabbitConfigProduct.jsonConverter();

        // Then
        assertNotNull(converter);
        assertTrue(converter instanceof org.springframework.amqp.support.converter.Jackson2JsonMessageConverter);
    }

    @Test
    void shouldCreateRabbitTemplate() {
        // Given
        org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory = 
                mock(org.springframework.amqp.rabbit.connection.ConnectionFactory.class);
        org.springframework.amqp.support.converter.MessageConverter messageConverter = 
                mock(org.springframework.amqp.support.converter.MessageConverter.class);

        // When
        RabbitTemplate template = rabbitConfigProduct.rabbitTemplate(connectionFactory, messageConverter);

        // Then
        assertNotNull(template);
        assertEquals(connectionFactory, template.getConnectionFactory());
        assertEquals(messageConverter, template.getMessageConverter());
    }
}
