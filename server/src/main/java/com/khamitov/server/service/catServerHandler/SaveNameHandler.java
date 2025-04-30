package com.khamitov.server.service.catServerHandler;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.callback.CallbackPrefix;
import com.khamitov.server.service.component.SaveNameComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveNameHandler implements CatServerHandler {

    private final SaveNameComponent saveNameComponent;
    private final TelegramProducer telegramProducer;

    @Override
    public CatAction getAction() {
        return CatAction.GET_CAT_SAVE_NAME;
    }

    @Override
    public void execute(CatServerDto messageDto) {
        CatDto catDto = messageDto.getCats().getFirst();
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.ACCEPT_CAT, catDto.getId().toString());
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getContextId())
                .text(saveNameComponent.getMessageText())
                .inlineKeyboard(saveNameComponent.getInlineKeyboard(catDto.getId()))
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }
}
