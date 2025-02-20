package com.khamitov.tgproject.model.dto;

import com.khamitov.tgproject.model.constant.ActionsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TelegramMessageDto {

    /**
     * Действие с чатом телеграма
     */
    private ActionsEnum action;
    /**
     * Идентификатор пользователя
     */
    private Long userId;
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Ид сообщения если это колбек (т.е в каком сообщении нажали кнопку)
     */
    private String messageId;
    /**
     * Текстовка сообщения (+команды)
     */
    private String text;
    /**
     * Текстовое id колбека (те же команды, но скрытые за кнопками в чате)
     */
    private String callback;
    /**
     * Урлы картинок
     */
    private String attachment;
    /**
     * Клавиатура которая под строкой ввода в тг
     */
    private List<List<TelegramMessageKeyboardDto>> replyKeyboard;
    /**
     * Кнопки в сообщении
     */
    private List<List<TelegramMessageKeyboardDto>> inlineKeyboard;
    /**
     * Закрепить сообщение после отправки
     */
    private Boolean pinMessage;
    /**
     * Открепить все сообщение после отправки
     */
    private Boolean unpinMessages;
}
