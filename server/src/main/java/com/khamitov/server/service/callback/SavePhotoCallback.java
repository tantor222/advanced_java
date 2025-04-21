package com.khamitov.server.service.callback;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.repository.CatRepository;
import com.khamitov.server.service.component.SavePhotoComponent;
import com.khamitov.server.service.telegram.TelegramProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavePhotoCallback implements CallbackHandler {

    private final TelegramProducer telegramProducer;
    private final SavePhotoComponent savePhotoComponent;
    private final CatRepository catRepository;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.SAVE_PHOTO.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        if (catId == null) {
            throw new RuntimeException("Cat id is not send");
        }
        Cat cat = catRepository.getCat(UUID.fromString(catId));
        String context = CallbackPrefix.createPrefix(ECallbackPrefixes.SAVE_NAME, catId);
        TelegramMessageDto response = TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .chatId(messageDto.getChatId())
                .text(savePhotoComponent.getMessageText())
                .inlineKeyboard(savePhotoComponent.getInlineKeyboard(cat.getId()))
                .context(context)
                .build();

        telegramProducer.sendMessage(response);
    }
}
