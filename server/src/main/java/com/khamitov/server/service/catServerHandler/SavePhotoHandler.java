package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import com.khamitov.server.service.component.SavePhotoComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavePhotoHandler implements CatServerHandler {

    private final SavePhotoComponent savePhotoComponent;
    private final TelegramProducer telegramProducer;

    @Override
    public CatAction getAction() {
        return CatAction.GET_CAT_SAVE_PHOTO;
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats().getFirst();
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.SAVE_NAME, catDto.getId().toString());
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getContextId())
                .text(savePhotoComponent.getMessageText())
                .inlineKeyboard(savePhotoComponent.getInlineKeyboard(catDto.getId()))
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }
}
