package com.khamitov.server.service.callback;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.catServer.CatServerProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavePhotoCallback implements CallbackHandler {

    private final CatServerProducer catServerProducer;

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
        CatDto catDto = CatDto.builder()
                .id(UUID.fromString(catId))
                .build();

        CatServerDto catServerDto = CatServerDto.builder()
                .action(CatAction.GET_CAT_SAVE_PHOTO)
                .contextId(messageDto.getChatId())
                .cats(List.of(catDto))
                .build();

        catServerProducer.sendMessage(catServerDto);
    }
}
