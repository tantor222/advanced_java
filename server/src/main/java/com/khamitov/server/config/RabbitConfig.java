package com.khamitov.server.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final QueueProperties queueProperties;

    @Bean
    public TopicExchange serverExchange() {
        return new TopicExchange(queueProperties.getExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue serverInQueue() {
        return new Queue(queueProperties.getInQueue());
    }

    @Bean
    public Queue serverOutQueue() {
        return new Queue(queueProperties.getOutQueue());
    }

    @Bean
    public Binding battleInBinding(
            @Qualifier("serverInQueue") Queue battleInQueue,
            @Qualifier("serverExchange") TopicExchange exchange
    ) {
        return BindingBuilder.bind(battleInQueue).to(exchange).with(queueProperties.getInQueue());
    }

    @Bean
    public Binding battleDocBinding(
            @Qualifier("serverOutQueue") Queue battleOutQueue,
            @Qualifier("serverExchange") TopicExchange exchange
    ) {
        return BindingBuilder.bind(battleOutQueue).to(exchange).with(queueProperties.getOutQueue());
    }
}
