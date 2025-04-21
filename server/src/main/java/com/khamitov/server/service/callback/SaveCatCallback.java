package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.repository.CatRepository;
import com.khamitov.server.service.component.SaveCatComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaveCatCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final SaveCatComponent saveCatComponent;
    private final CatRepository catRepository;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.SAVE_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        if (catId == null) {
            throw new RuntimeException("Cat id is not send");
        }
        Cat cat = catRepository.getCat(UUID.fromString(catId));
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_PHOTO)
                .chatId(messageDto.getChatId())
                .text(saveCatComponent.getMessageText(cat.getName(), cat.getAuthorName()))
                .inlineKeyboard(saveCatComponent.getInlineKeyboard(1L))
                .attachment(cat.getFilePath())
                .build();

        telegramProducer.sendMessage(response);
    }
}
