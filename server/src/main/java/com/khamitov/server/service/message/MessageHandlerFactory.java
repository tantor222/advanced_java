package com.khamitov.server.service.message;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHandlerFactory {

    private final List<MessageHandler> messageHandlers;
    private final TelegramProducer telegramProducer;

    public MessageHandler getHandler(TelegramMessageDto messageDto) {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.getMessage().equals(messageDto.getText())) {
                return messageHandler;
            }
        }
        return null;
    }
}
