package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.service.component.CatListComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatListHandler implements CatServerHandler {

    private final TelegramProducer telegramProducer;
    private final CatListComponent catListComponent;

    @Override
    public CatAction getAction() {
        return CatAction.GET_ALL_CAT_LIST;
    }

    @Override
    public void execute(CatServerDto messageDto) {
        List<CatDto> cats = messageDto.getCats();
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getContextId())
                .text(catListComponent.getMessageText())
                .inlineKeyboard(catListComponent.getInlineKeyboard(messageDto.getPage(), cats))
                .build();

        telegramProducer.sendMessage(response);
    }
}
