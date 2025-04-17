package com.khamitov.server.service.telegram;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.config.QueueProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramProducer {

    private final RabbitTemplate template;
    private final QueueProperties queueProperties;

    public void sendMessage(TelegramMessageDto message) {
        template.convertAndSend(queueProperties.getOutQueue(), message);
    }

    public void sendErrorMessage(TelegramMessageDto incomingMessage, String text) {
        var response = TelegramMessageDto.builder()
                .chatId(incomingMessage.getChatId())
                .text(text)
                .build();
        sendMessage(response);
    }
}
