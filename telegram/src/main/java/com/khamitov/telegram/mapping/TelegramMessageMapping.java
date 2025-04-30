package com.khamitov.telegram.mapping;

import com.khamitov.model.dto.TelegramMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TelegramMessageMapping {

    @Mapping(target = "chatId", source = "update.message.from.id")
    @Mapping(target = "text", source = "update.message.text")
    @Mapping(target = "nickname", source = "update.message.chat.userName")
    @Mapping(target = "context", source = "context")
    TelegramMessageDto fromMessage(Update update, String context);

    @Mapping(target = "chatId", source = "update.callbackQuery.from.id")
    @Mapping(target = "messageId", source = "update.callbackQuery.message.messageId")
    @Mapping(target = "callback", source = "update.callbackQuery.data")
    @Mapping(target = "nickname", source = "update.callbackQuery.from.userName")
    @Mapping(target = "context", source = "context")
    TelegramMessageDto fromCallback(Update update, String context);

    @Mapping(target = "chatId", source = "update.message.from.id")
    @Mapping(target = "text", source = "update.message.text")
    @Mapping(target = "attachment", source = "photo.absolutePath")
    @Mapping(target = "context", source = "context")
    TelegramMessageDto fromPhoto(Update update, File photo, String context);
}
