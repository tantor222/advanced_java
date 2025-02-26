package com.khamitov.tgproject.model.dto;

import com.khamitov.tgproject.model.constant.ActionsEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class TelegramMessageDto {

    private ActionsEnum action;
    private Long chatId;
    private String messageId;
    private String text;
    private String callback;
    private String attachment;
    private List<List<TelegramMessageKeyboardDto>> replyKeyboard;
    private List<List<TelegramMessageKeyboardDto>> inlineKeyboard;
}
