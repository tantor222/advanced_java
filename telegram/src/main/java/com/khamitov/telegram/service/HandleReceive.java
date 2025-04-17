package com.khamitov.telegram.service;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.telegram.config.QueueProperties;
import com.khamitov.telegram.mapping.TelegramMessageMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleReceive {

    private final RabbitTemplate template;
    private final TelegramMessageMapping telegramMessageMapping;
    private final QueueProperties queueProperties;

    public void handleMessage(Update update) {
        TelegramMessageDto message = telegramMessageMapping.fromMessage(update);
        template.convertAndSend(queueProperties.getInQueue(), message);
    }

    public void handlePhoto(Update update, File photoFile) {
        TelegramMessageDto message = telegramMessageMapping.fromPhoto(update, photoFile);
        template.convertAndSend(queueProperties.getInQueue(), message);
    }

    public void handleCallback(Update update) {
        TelegramMessageDto message = telegramMessageMapping.fromCallback(update);
        template.convertAndSend(queueProperties.getInQueue(), message);
    }
}
