package com.khamitov.server.service.callback;

import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.server.constant.ECallbackPrefixes;
import com.khamitov.server.service.message.StartMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainMenuCallback implements CallbackHandler {

    private final StartMessageHandler startMessageHandler;

    @Override
    public String getPrefix() {
        return ECallbackPrefixes.MAIN_MENU.getPref();
    }

    @Override
    public void execute(TelegramMessageDto messageDto) {
        startMessageHandler.execute(messageDto);
    }
}
