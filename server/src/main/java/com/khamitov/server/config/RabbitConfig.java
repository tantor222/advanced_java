package com.khamitov.server.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange serverExchange(QueueProperties queueProperties) {
        return new TopicExchange(queueProperties.getExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue serverInQueue(QueueProperties queueProperties) {
        return new Queue(queueProperties.getInQueue());
    }

    @Bean
    public Queue serverOutQueue(QueueProperties queueProperties) {
        return new Queue(queueProperties.getOutQueue());
    }

    @Bean
    public Binding serverInBinding(
            Queue serverInQueue,
            TopicExchange serverExchange,
            QueueProperties queueProperties
    ) {
        return BindingBuilder.bind(serverInQueue).to(serverExchange).with(queueProperties.getInQueue());
    }

    @Bean
    public Binding serverOutBinding(
            Queue serverOutQueue,
            TopicExchange serverExchange,
            QueueProperties queueProperties
    ) {
        return BindingBuilder.bind(serverOutQueue).to(serverExchange).with(queueProperties.getOutQueue());
    }
}
