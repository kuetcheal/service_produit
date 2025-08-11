package com.javathinked.example.demo_spring.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfigProduct {

    public static final String EXCHANGE = "app.exchange";

    // Queue écoutée par le service PRODUIT (ce que commande publie via "order.*")
    public static final String Q_FROM_ORDER = "q.product.from-order";

    @Bean
    TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue qFromOrder() {
        return new Queue(Q_FROM_ORDER, true);
    }

    @Bean
    Binding bindOrder(Queue qFromOrder, TopicExchange appExchange) {
        return BindingBuilder.bind(qFromOrder).to(appExchange).with("order.*");
    }

    // JSON
    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter converter) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(converter);
        return t;
    }
}
