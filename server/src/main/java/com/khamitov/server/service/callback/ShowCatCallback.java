package com.khamitov.server.service.callback;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.CatDto;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.catServer.CatServerProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowCatCallback implements CallbackHandler {

    private final CatServerProducer catServerProducer;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.SHOW_CAT.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        String catId = CallbackPrefix.getPath(messageDto.getCallback());
        List<CatDto> catDtos;
        if (catId != null) {
            catDtos = List.of(CatDto.builder()
                    .id(UUID.fromString(catId))
                    .build());
        } else {
            catDtos = Collections.emptyList();
        }
        CatServerDto catServerDto = CatServerDto.builder()
                .action(CatAction.SHOW_CAT)
                .contextId(messageDto.getChatId())
                .cats(catDtos)
                .build();

        catServerProducer.sendMessage(catServerDto);
    }
}
