package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.component.CatListComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatListCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final CatListComponent catListComponent;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.CAT_LIST.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        Integer page = Optional.of(messageDto.getCallback())
                .map(CallbackPrefix::getPath)
                .filter(Objects::nonNull)
                .map(Integer::valueOf)
                .orElse(0);

        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(catListComponent.getMessageText())
                .inlineKeyboard(catListComponent.getInlineKeyboard(page, Collections.emptyList()))
                .build();

        telegramProducer.sendMessage(response);
    }
}
