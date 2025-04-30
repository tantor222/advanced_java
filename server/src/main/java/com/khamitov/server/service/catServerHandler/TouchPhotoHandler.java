package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.service.component.TouchPhotoComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TouchPhotoHandler implements CatServerHandler {

    private final TouchPhotoComponent touchPhotoComponent;
    private final TelegramProducer telegramProducer;

    @Override
    public CatAction getAction() {
        return CatAction.GET_CAT_TOUCH_PHOTO;
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats().getFirst();
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getContextId())
                .text(touchPhotoComponent.getMessageText())
                .inlineKeyboard(touchPhotoComponent.getInlineKeyboard(catDto.getId()))
                .build();

        telegramProducer.sendMessage(response);
    }
}
