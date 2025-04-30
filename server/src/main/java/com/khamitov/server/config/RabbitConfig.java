package com.khamitov.server.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange serverExchange(ServerQueueProperties queueProperties) {
        return new TopicExchange(queueProperties.getExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue serverInQueue(ServerQueueProperties queueProperties) {
        return new Queue(queueProperties.getInQueue());
    }

    @Bean
    public Queue serverOutQueue(ServerQueueProperties queueProperties) {
        return new Queue(queueProperties.getOutQueue());
    }

    @Bean
    public Binding serverInBinding(
            Queue serverInQueue,
            TopicExchange serverExchange,
            ServerQueueProperties queueProperties
    ) {
        return BindingBuilder.bind(serverInQueue).to(serverExchange).with(queueProperties.getInQueue());
    }

    @Bean
    public Binding serverOutBinding(
            Queue serverOutQueue,
            TopicExchange serverExchange,
            ServerQueueProperties queueProperties
    ) {
        return BindingBuilder.bind(serverOutQueue).to(serverExchange).with(queueProperties.getOutQueue());
    }

    @Bean
    public Queue catServerInQueue(CatServerQueueProperties queueProperties) {
        return new Queue(queueProperties.getInQueue());
    }

    @Bean
    public Queue catServerOutQueue(CatServerQueueProperties queueProperties) {
        return new Queue(queueProperties.getOutQueue());
    }

    @Bean
    public Binding catServerInBinding(
            @Qualifier("catServerInQueue") Queue serverInQueue,
            TopicExchange serverExchange,
            CatServerQueueProperties queueProperties
    ) {
        return BindingBuilder.bind(serverInQueue).to(serverExchange).with(queueProperties.getInQueue());
    }

    @Bean
    public Binding catServerOutBinding(
            @Qualifier("catServerOutQueue") Queue serverOutQueue,
            TopicExchange serverExchange,
            CatServerQueueProperties queueProperties
    ) {
        return BindingBuilder.bind(serverOutQueue).to(serverExchange).with(queueProperties.getOutQueue());
    }
}
