package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.repository.CatRepository;
import com.khamitov.server.service.component.TouchPhotoComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TouchPhotoCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final TouchPhotoComponent touchPhotoComponent;
    private final CatRepository catRepository;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.TOUCH_PHOTO.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getContext());
        if (catId == null) {
            throw new RuntimeException("Cat id is not send");
        }
        Cat cat = catRepository.getCat(UUID.fromString(catId));
        cat.setFilePath(messageDto.getAttachment()); // TODO replace for db

        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(touchPhotoComponent.getMessageText())
                .inlineKeyboard(touchPhotoComponent.getInlineKeyboard(cat.getId()))
                .build();

        telegramProducer.sendMessage(response);
    }
}
