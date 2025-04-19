package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.component.AcceptCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcceptCatCallback implements CallbackHandler {

    private final AcceptCatComponent acceptCatComponent;
    private final TelegramProducer telegramProducer;
    private final MainMenuCallback mainMenuCallback;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.ACCEPT_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(acceptCatComponent.getMessageText())
                .build();

        telegramProducer.sendMessage(response);
        mainMenuCallback.execute(messageDto);
    }
}
