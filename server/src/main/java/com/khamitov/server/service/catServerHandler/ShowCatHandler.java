package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import com.khamitov.server.service.component.ShowCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowCatHandler implements CatServerHandler{

    private final TelegramProducer telegramProducer;
    private final ShowCatComponent showCatComponent;

    @Override
    public CatAction getAction() {
        return CatAction.SHOW_CAT;
    }

    @Override
    public void execute(CatServerDto messageDto) {
        List<CatDto> cats = messageDto.getCats();
        CatDto cat = cats.stream()
                .findAny()
                .orElseThrow();
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.SAVE_NAME);
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_PHOTO)
                .chatId(messageDto.getContextId())
                .text(showCatComponent.getMessageText(cat))
                .inlineKeyboard(showCatComponent.getInlineKeyboard(cat))
                .context(context)
                .attachment(cat.getFilePath())
                .build();

        telegramProducer.sendMessage(response);
    }
}
