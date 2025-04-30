package com.khamitov.server.service.catServer;

import com.khamitov.model.dto.CatServerDto;
import com.khamitov.server.service.catServerHandler.CatServerHandler;
import com.khamitov.server.service.catServerHandler.CatServerHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatServerConsumer {

    private final CatServerHandlerFactory catServerHandlerFactory;

    @RabbitListener(queues = "${spring.rabbitmq.cat-server.out-queue}")
    public void consume(CatServerDto message) {
        log.info("Received [queue: cat-server[OUT]], [message: {}]", message);
        try {
            if (message.getAction() != null) {
                CatServerHandler messageHandler = catServerHandlerFactory.getHandler(message);
                if (messageHandler != null) {
                    messageHandler.execute(message);
                    return;
                }
            }
            log.error("Invalid action to receive: {}", message);
        } catch (Exception e) {
            log.error("Receive message with error: ", e);
        }
    }
}
