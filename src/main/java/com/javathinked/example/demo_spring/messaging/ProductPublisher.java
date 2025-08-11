package com.javathinked.example.demo_spring.messaging;

import com.javathinked.example.demo_spring.config.RabbitConfigProduct;
import com.javathinked.example.demo_spring.dto.events.ProductStockUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProductPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishStockUpdated(ProductStockUpdatedEvent evt) {
        // Les consommateurs (ex: service_commande) sont bindés à "product.stock.*"
        rabbitTemplate.convertAndSend(RabbitConfigProduct.EXCHANGE, "product.stock.updated", evt);
    }
}
