package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.component.SaveNameComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveNameCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final SaveNameComponent saveNameComponent;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.SAVE_NAME.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(saveNameComponent.getMessageText())
                .inlineKeyboard(saveNameComponent.getInlineKeyboard(1L))
                .build();

        telegramProducer.sendMessage(response);
    }
}
