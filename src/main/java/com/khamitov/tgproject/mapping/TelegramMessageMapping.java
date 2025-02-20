package com.khamitov.tgproject.mapping;

import com.khamitov.tgproject.model.dto.TelegramMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.telegram.telegrambots.meta.api.objects.Update;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TelegramMessageMapping {

    @Mapping(target = "userId", source = "update.message.from.id")
    @Mapping(target = "username", source = "update.message.chat.userName")
    @Mapping(target = "text", source = "update.message.text")
    TelegramMessageDto fromMessage(Update update);

    @Mapping(target = "userId", source = "update.callbackQuery.from.id")
    @Mapping(target = "messageId", source = "update.callbackQuery.message.messageId")
    @Mapping(target = "callback", source = "update.callbackQuery.data")
    TelegramMessageDto fromCallback(Update update);
}
