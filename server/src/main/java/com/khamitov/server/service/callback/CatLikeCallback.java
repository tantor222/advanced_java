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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatLikeCallback implements CallbackHandler {

    private final CatServerProducer catServerProducer;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.LIKE_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        CatDto catDto = CatDto.builder()
                .id(Optional.ofNullable(catId)
                        .map(UUID::fromString)
                        .orElseThrow(() -> new RuntimeException("Cat id is not send")))
                .build();

        CatServerDto catServerDto = CatServerDto.builder()
                .action(CatAction.GET_CAT_LIKE)
                .contextId(messageDto.getChatId())
                .cats(List.of(catDto))
                .build();

        catServerProducer.sendMessage(catServerDto);
    }
}
