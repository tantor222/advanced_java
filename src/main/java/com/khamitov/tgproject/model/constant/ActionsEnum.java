package com.khamitov.tgproject.model.constant;

@SuppressWarnings("unused")
public enum ActionsEnum {
    /**
     * Отправить текстовое сообщение
     */
    SEND_MESSAGE,
    /**
     * Изменить текстовое сообщение
     */
    EDIT_MESSAGE,
    /**
     * Отправить что-то там еще
     */
    SEND_MEDIA,
    /**
     * Отправить картинку с текстом
     */
    SEND_PHOTO,
    /**
     * Изменить картинку
     */
    EDIT_MEDIA,
    /**
     * Изменить текст под картинкой
     */
    EDIT_MEDIA_CAPTION,
    /**
     * Изменить клавиатуру
     */
    EDIT_MESSAGE_REPLY_MARKUP,
}
