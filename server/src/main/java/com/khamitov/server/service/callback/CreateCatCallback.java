package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.repository.CatRepository;
import com.khamitov.server.service.component.CreateCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCatCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final CreateCatComponent createCatComponent;
    private final CatRepository catRepository;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.CREATE_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        Cat cat = catId != null ? catRepository.getCat(UUID.fromString(catId)) : createCat(messageDto);
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.TOUCH_PHOTO, String.valueOf(cat.getId()));
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(createCatComponent.getMessageText())
                .inlineKeyboard(createCatComponent.getInlineKeyboard())
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }

    private Cat createCat(TelegramMessageDto messageDto) {
        Cat cat = Cat.builder()
                .id(UUID.randomUUID())
                .filePath(messageDto.getAttachment())
                .authorChatId(messageDto.getChatId())
                .authorName(messageDto.getNickname())
                .likes(0)
                .dislikes(0)
                .build();
        catRepository.saveCat(cat);
        return cat;
    }
}
