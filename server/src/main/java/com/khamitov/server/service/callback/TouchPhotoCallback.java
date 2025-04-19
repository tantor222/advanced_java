package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.component.TouchPhotoComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TouchPhotoCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final TouchPhotoComponent touchPhotoComponent;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.TOUCH_PHOTO.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(touchPhotoComponent.getMessageText())
                .inlineKeyboard(touchPhotoComponent.getInlineKeyboard(1L))
                .build();

        telegramProducer.sendMessage(response);
    }
}
