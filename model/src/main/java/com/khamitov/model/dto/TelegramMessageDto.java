package com.khamitov.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TelegramMessageDto {

    private ActionsEnum action;
    private Long chatId;
    private String messageId;
    private String text;
    private String callback;
    private String attachment;
    private List<List<TelegramMessageKeyboardDto>> replyKeyboard;
    private List<List<TelegramMessageKeyboardDto>> inlineKeyboard;

    public static TelegramMessageDtoBuilder builder() {
        return new TelegramMessageDtoBuilder();
    }

    public static class TelegramMessageDtoBuilder {

        private ActionsEnum action;
        private Long chatId;
        private String messageId;
        private String text;
        private String callback;
        private String attachment;
        private List<List<TelegramMessageKeyboardDto>> replyKeyboard;
        private List<List<TelegramMessageKeyboardDto>> inlineKeyboard;

        public TelegramMessageDtoBuilder() {}

        public TelegramMessageDto build() {
            return new TelegramMessageDto(
                    action,
                    chatId,
                    messageId,
                    text,
                    callback,
                    attachment,
                    replyKeyboard,
                    inlineKeyboard
            );
        }

        public TelegramMessageDtoBuilder action(ActionsEnum action) {
            this.action = action;
            return this;
        }

        public TelegramMessageDtoBuilder chatId(Long chatId) {
            this.chatId = chatId;
            return this;
        }

        public TelegramMessageDtoBuilder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public TelegramMessageDtoBuilder text(String text) {
            this.text = text;
            return this;
        }

        public TelegramMessageDtoBuilder callback(String callback) {
            this.callback = callback;
            return this;
        }

        public TelegramMessageDtoBuilder attachment(String attachment) {
            this.attachment = attachment;
            return this;
        }

        public TelegramMessageDtoBuilder replyKeyboard(
                List<List<TelegramMessageKeyboardDto>> replyKeyboard) {
            this.replyKeyboard = replyKeyboard;
            return this;
        }

        public TelegramMessageDtoBuilder inlineKeyboard(
                List<List<TelegramMessageKeyboardDto>> inlineKeyboard) {
            this.inlineKeyboard = inlineKeyboard;
            return this;
        }
    }
}
