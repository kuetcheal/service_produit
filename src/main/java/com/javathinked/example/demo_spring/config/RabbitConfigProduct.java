package com.javathinked.example.demo_spring.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfigProduct {

    public static final String EXCHANGE = "app.exchange";

    // Queue écoutée par le service PRODUIT (ce que COMMANDE publie via "order.*")
    public static final String Q_FROM_ORDER = "q.product.from-order";
    public static final String RK_ORDER_ALL = "order.*";

    @Bean
    TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean(name = "qProductFromOrder")
    Queue qFromOrder() {
        return new Queue(Q_FROM_ORDER, true);
    }

    @Bean
    Binding bindOrder(@Qualifier("qProductFromOrder") Queue qFromOrder, TopicExchange appExchange) {
        return BindingBuilder.bind(qFromOrder).to(appExchange).with(RK_ORDER_ALL);
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
