package com.khamitov.server.service.message;

import com.khamitov.model.dto.TelegramMessageDto;

public interface MessageHandler {

    String getMessage();

    void execute(TelegramMessageDto messageDto);
}
