package com.khamitov.cat_server.service.handler;

import com.khamitov.model.dto.CatServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHandlerFactory {

    private final List<MessageHandler> messageHandlers;

    public MessageHandler getHandler(CatServerDto messageDto) {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.getActions().contains(messageDto.getAction())) {
                return messageHandler;
            }
        }
        return null;
    }
}
