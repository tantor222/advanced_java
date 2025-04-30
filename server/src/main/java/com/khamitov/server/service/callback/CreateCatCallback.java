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
public class CreateCatCallback implements CallbackHandler {

    private final CatServerProducer catServerProducer;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.CREATE_CAT.getPref();
    }

    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        CatDto catDto = CatDto.builder()
                .id(Optional.ofNullable(catId).map(UUID::fromString).orElse(null))
                .authorName(messageDto.getNickname())
                .authorChatId(messageDto.getChatId())
                .build();

        CatServerDto catServerDto = CatServerDto.builder()
                .action(CatAction.GET_OR_CREATE_CAT)
                .contextId(messageDto.getChatId())
                .cats(List.of(catDto))
                .build();

        catServerProducer.sendMessage(catServerDto);
    }
}
