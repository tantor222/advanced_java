package com.khamitov.server.service.catServer;

import com.khamitov.model.dto.CatServerDto;
import com.khamitov.server.config.CatServerQueueProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatServerProducer {

    private final RabbitTemplate template;
    private final CatServerQueueProperties queueProperties;

    public void sendMessage(CatServerDto message) {
        template.convertAndSend(queueProperties.getInQueue(), message);
    }
}
