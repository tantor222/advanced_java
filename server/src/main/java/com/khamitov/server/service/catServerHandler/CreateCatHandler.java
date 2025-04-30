package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import com.khamitov.server.service.component.CreateCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCatHandler implements CatServerHandler {

    private final CreateCatComponent createCatComponent;
    private final TelegramProducer telegramProducer;

    @Override
    public CatAction getAction() {
        return CatAction.GET_OR_CREATE_CAT;
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats().getFirst();
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.TOUCH_PHOTO, String.valueOf(catDto.getId()));
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getContextId())
                .text(createCatComponent.getMessageText())
                .inlineKeyboard(createCatComponent.getInlineKeyboard())
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }
}
