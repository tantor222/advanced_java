package com.khamitov.telegram.service;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.telegram.config.QueueProperties;
import com.khamitov.telegram.mapping.TelegramMessageMapping;
import com.khamitov.telegram.repository.TelegramContextRepository;
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
    private final TelegramContextRepository telegramContextRepository;
    private final QueueProperties queueProperties;

    public void handleMessage(Update update) {
        String context = getContext(update.getMessage().getFrom().getId());
        TelegramMessageDto message = telegramMessageMapping.fromMessage(update, context);
        template.convertAndSend(queueProperties.getInQueue(), message);
    }

    public void handlePhoto(Update update, File photoFile) {
        String context = getContext(update.getMessage().getFrom().getId());
        TelegramMessageDto message = telegramMessageMapping.fromPhoto(update, photoFile, context);
        template.convertAndSend(queueProperties.getInQueue(), message);
    }

    public void handleCallback(Update update) {
        String context = getContext(update.getCallbackQuery().getFrom().getId());
        TelegramMessageDto message = telegramMessageMapping.fromCallback(update, context);
        template.convertAndSend(queueProperties.getInQueue(), message);
    }

    private String getContext(Long chatId) {
        String context = telegramContextRepository.getContext(chatId);
        if (context != null) {
            telegramContextRepository.removeContext(chatId);
            return context;
        }
        return null;
    }
}
