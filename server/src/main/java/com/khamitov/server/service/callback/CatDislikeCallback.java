package com.khamitov.server.service.callback;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.model.entity.Cat;
import com.khamitov.server.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatDislikeCallback implements CallbackHandler {

    private final ShowCatCallback showCatCallback;
    private final CatRepository catRepository;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.DISLIKE_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        if (catId == null) {
            throw new RuntimeException("Cat id is not send");
        }
        Cat cat = catRepository.getCat(UUID.fromString(catId));
        cat.setDislikes(cat.getDislikes() + 1);
        showCatCallback.executeNext(messageDto, cat.getId());
    }
}
