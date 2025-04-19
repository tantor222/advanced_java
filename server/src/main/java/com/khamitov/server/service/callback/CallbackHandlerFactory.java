package com.khamitov.server.service.callback;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackHandlerFactory {

    private final List<CallbackHandler> callbackHandlers;
    private final TelegramProducer telegramProducer;

    public CallbackHandler getHandler(TelegramMessageDto messageDto) {
        String prefix = Optional.ofNullable(CallbackPrefix.getPrefix(messageDto.getCallback()))
                .orElseThrow();
        for (CallbackHandler callbackHandler : callbackHandlers) {
            if (callbackHandler.getPrefix().equals(prefix)) {
                return callbackHandler;
            }
        }
        return null;
    }

    public CallbackHandler getHandlerFromContext(TelegramMessageDto messageDto) {
        String prefix = Optional.ofNullable(CallbackPrefix.getPrefix(messageDto.getContext()))
                .orElseThrow();
        for (CallbackHandler callbackHandler : callbackHandlers) {
            if (callbackHandler.getPrefix().equals(prefix)) {
                return callbackHandler;
            }
        }
        return null;
    }
}
