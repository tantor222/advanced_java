package com.khamitov.server.service.message;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.component.MainMenuComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartMessageHandler implements MessageHandler {

    private final TelegramProducer telegramProducer;
    private final MainMenuComponent mainMenuComponent;

    @Override
    public String getMessage() {
        return ECallbackPrefixes.START.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(mainMenuComponent.getMessageText())
                .inlineKeyboard(mainMenuComponent.getInlineKeyboard())
                .build();

        telegramProducer.sendMessage(response);
    }

    public void execute(CatServerDto messageDto) {
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getContextId())
                .text(mainMenuComponent.getMessageText())
                .inlineKeyboard(mainMenuComponent.getInlineKeyboard())
                .build();

        telegramProducer.sendMessage(response);
    }
}
