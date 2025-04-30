package com.khamitov.server.service.callback;

import com.khamitov.model.dto.TelegramMessageDto;

public interface CallbackHandler {

    String getPrefix();

    void execute(TelegramMessageDto messageDto);
}
