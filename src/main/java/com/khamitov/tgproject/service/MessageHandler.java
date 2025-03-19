package com.khamitov.tgproject.service;

import com.khamitov.tgproject.model.constant.TextConstants;
import com.khamitov.tgproject.model.constant.InlineButtonCallbacks;
import com.khamitov.tgproject.model.dto.TelegramMessageDto;
import com.khamitov.tgproject.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.tgproject.model.constant.ActionsEnum;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHandler {

    private final Map<String, Function<TelegramMessageDto, TelegramMessageDto>> consumers = new HashMap<>();

    @PostConstruct
    public void init() {
        consumers.put("/start", this::start);
    }

    public TelegramMessageDto handleCommands(TelegramMessageDto message) {
        try {
            return Optional.ofNullable(consumers.get(message.getText()))
                    .map(fn -> fn.apply(message))
                    .orElse(null);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage());
            return null;
        }
    }

    private TelegramMessageDto start(TelegramMessageDto telegramMessageDto) {
        TelegramMessageKeyboardDto button = TelegramMessageKeyboardDto.builder()
                .text(InlineButtonCallbacks.SHOW_IMAGES.getText())
                .callback(InlineButtonCallbacks.SHOW_IMAGES.getCallback())
                .build();

        return TelegramMessageDto.builder()
                .chatId(telegramMessageDto.getChatId())
                .text(TextConstants.START_MESSAGE)
                .inlineKeyboard(List.of(List.of(button)))
                .action(ActionsEnum.SEND_MESSAGE)
                .build();
    }
}
