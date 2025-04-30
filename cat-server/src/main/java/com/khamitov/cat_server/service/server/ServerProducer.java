package com.khamitov.cat_server.service.server;

import com.khamitov.cat_server.config.QueueProperties;
import com.khamitov.model.dto.CatServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerProducer {

    private final RabbitTemplate template;
    private final QueueProperties queueProperties;

    public void sendMessage(CatServerDto message) {
        template.convertAndSend(queueProperties.getOutQueue(), message);
    }
}
