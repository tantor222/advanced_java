package com.khamitov.server.service.component;

import com.khamitov.model.dto.TelegramMessageKeyboardDto;

import java.util.List;

public class AbstractMessageComponent {

    public String getMessageText() {
        return "";
    }

    public List<List<TelegramMessageKeyboardDto>> getInlineKeyboard() {
        return null;
    }
}
