package com.khamitov.tgproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramMessageKeyboardDto {
    /**
     * Текстовка кнопки
     */
    private String text;
    /**
     * Если кнопка в сообщении то при нажатии текст из этого поля придет в поле коллбека (как команда)
     */
    private String callback;
    /**
     * ссылка на мини-апп приложение
     */
    private String appUrl;
}
