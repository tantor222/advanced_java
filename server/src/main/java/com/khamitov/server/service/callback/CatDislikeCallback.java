package com.khamitov.server.service.callback;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatDislikeCallback implements CallbackHandler {

    private final ShowCatCallback showCatCallback;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.LIKE_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        Cat cat = Cat.builder()
                .id(UUID.randomUUID())
                .name("LUCE")
                .likes(22)
                .dislikes(4)
                .authorName("Borg")
                .authorChatId(1234L)
                .build();
        showCatCallback.executeNext(messageDto, cat);
    }
}
