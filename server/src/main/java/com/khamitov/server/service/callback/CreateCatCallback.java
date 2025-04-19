package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.component.CreateCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCatCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final CreateCatComponent createCatComponent;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.CREATE_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.TOUCH_PHOTO);
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(createCatComponent.getMessageText())
                .inlineKeyboard(createCatComponent.getInlineKeyboard())
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }
}
