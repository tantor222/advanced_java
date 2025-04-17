package com.khamitov.server.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq.server")
public class QueueProperties {

    private String exchange;
    private String inQueue;
    private String outQueue;
}
