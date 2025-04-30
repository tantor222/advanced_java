package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.service.component.SaveCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveCatHandler implements CatServerHandler {

    private final SaveCatComponent saveCatComponent;
    private final TelegramProducer telegramProducer;

    @Override
    public CatAction getAction() {
        return CatAction.GET_CAT_SAVE;
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats().getFirst();
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_PHOTO)
                .chatId(messageDto.getContextId())
                .text(saveCatComponent.getMessageText(catDto.getName(), catDto.getAuthorName()))
                .inlineKeyboard(saveCatComponent.getInlineKeyboard(catDto.getId()))
                .attachment(catDto.getFilePath())
                .build();

        telegramProducer.sendMessage(response);
    }
}
