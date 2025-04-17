package com.khamitov.server.service.telegram;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.service.callback.CallbackHandlerFactory;
import com.khamitov.server.service.message.MessageHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramConsumer {

    private final MessageHandlerFactory messageHandlerFactory;
    private final CallbackHandlerFactory callbackHandlerFactory;
    private final TelegramProducer telegramProducer;

    @RabbitListener(queues = "${spring.rabbitmq.server.in-queue}")
    public void consume(TelegramMessageDto message) {
        log.info("Received [queue: server], [message: {}]", message);
        try {
            if (message.getText() != null) {
                messageHandlerFactory.execute(message);
            } else if (message.getCallback() != null) {
                callbackHandlerFactory.execute(message);
            } else {
                telegramProducer.sendErrorMessage(message, "Invalid action to receive");
            }
        } catch (Exception e) {
            log.error("Receive message with error: ", e);
        }
    }
}
