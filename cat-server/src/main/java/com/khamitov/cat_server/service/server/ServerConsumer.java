package com.khamitov.cat_server.service.server;

import com.khamitov.cat_server.service.handler.MessageHandler;
import com.khamitov.cat_server.service.handler.MessageHandlerFactory;
import com.khamitov.model.dto.CatServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerConsumer {

    private final MessageHandlerFactory messageHandlerFactory;

    @RabbitListener(queues = "${spring.rabbitmq.cat-server.in-queue}")
    public void consume(CatServerDto message) {
        log.info("Received [queue: cat-server[IN]], [message: {}]", message);
        try {
            if (message.getAction() != null) {
                MessageHandler messageHandler = messageHandlerFactory.getHandler(message);
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
