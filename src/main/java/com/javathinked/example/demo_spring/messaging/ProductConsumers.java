package com.javathinked.example.demo_spring.messaging;

import com.javathinked.example.demo_spring.config.RabbitConfigProduct;
import com.javathinked.example.demo_spring.dto.events.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumers {

    // Reçoit les events publiés par service_commande (routingKey "order.*")
    @RabbitListener(queues = RabbitConfigProduct.Q_FROM_ORDER)
    public void onOrderCreated(OrderCreatedEvent evt) {
        // Ici tu peux réserver le stock / logger / déclencher un traitement
        System.out.println("ProductService <- Order created: " + evt);
    }
}
