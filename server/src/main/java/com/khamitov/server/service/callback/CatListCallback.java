package com.khamitov.server.service.callback;

import com.khamitov.model.constant.CatAction;
import com.khamitov.model.dto.CatServerDto;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.catServer.CatServerProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatListCallback implements CallbackHandler {

    private final CatServerProducer catServerProducer;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.CAT_LIST.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        Integer page = Optional.of(messageDto.getCallback())
                .map(CallbackPrefix::getPath)
                .filter(Objects::nonNull)
                .map(Integer::valueOf)
                .orElse(0);

        CatServerDto catServerDto = CatServerDto.builder()
                .action(CatAction.GET_ALL_CAT_LIST)
                .contextId(messageDto.getChatId())
                .page(page)
                .build();

        catServerProducer.sendMessage(catServerDto);
    }
}
