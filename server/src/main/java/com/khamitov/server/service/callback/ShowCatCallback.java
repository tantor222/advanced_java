package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.service.component.ShowCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowCatCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final ShowCatComponent showCatComponent;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.SHOW_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        Cat cat = Cat.builder()
                .id(UUID.randomUUID())
                .name("MAX")
                .likes(10)
                .dislikes(2)
                .authorName("Borg")
                .authorChatId(1234L)
                .build();
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.SAVE_NAME);
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(showCatComponent.getMessageText(cat))
                .inlineKeyboard(showCatComponent.getInlineKeyboard(cat))
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }

    public void executeNext(TelegramMessageDto messageDto, Cat cat) {
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.SAVE_NAME);
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(showCatComponent.getMessageText(cat))
                .inlineKeyboard(showCatComponent.getInlineKeyboard(cat))
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }
}
