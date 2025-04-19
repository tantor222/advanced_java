package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.component.SavePhotoComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavePhotoCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final SavePhotoComponent savePhotoComponent;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.SAVE_PHOTO.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.SAVE_NAME);
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(savePhotoComponent.getMessageText())
                .inlineKeyboard(savePhotoComponent.getInlineKeyboard(1L))
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }
}
