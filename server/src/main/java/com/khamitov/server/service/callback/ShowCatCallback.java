package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.repository.CatRepository;
import com.khamitov.server.service.component.ShowCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowCatCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final ShowCatComponent showCatComponent;
    private final CatRepository catRepository;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.SHOW_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        List<Cat> cats = catRepository.getAllCats();
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        Cat cat;
        if (catId != null) {
            cat = cats.stream()
                    .filter(c -> !UUID.fromString(catId).equals(c.getId()))
                    .findAny()
                    .orElseThrow();
        } else {
            cat = cats.stream()
                    .findAny()
                    .orElseThrow();
        }
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_PHOTO)
                .chatId(messageDto.getChatId())
                .text(showCatComponent.getMessageText(cat))
                .inlineKeyboard(showCatComponent.getInlineKeyboard(cat))
                .attachment(cat.getFilePath())
                .build();

        telegramProducer.sendMessage(response);
    }

    public void executeNext(TelegramMessageDto messageDto, UUID catId) {
        List<Cat> cats = catRepository.getAllCats();
        Cat cat = cats.stream()
                .filter(c -> !catId.equals(c.getId()))
                .findAny()
                .orElseThrow();
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.SAVE_NAME);
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(showCatComponent.getMessageText(cat))
                .inlineKeyboard(showCatComponent.getInlineKeyboard(cat))
                .context(context)
                .attachment(cat.getFilePath())
                .build();

        telegramProducer.sendMessage(response);
    }
}
