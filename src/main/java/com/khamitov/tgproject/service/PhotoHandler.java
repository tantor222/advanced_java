package com.khamitov.tgproject.service;

import com.khamitov.tgproject.mapping.TelegramMessageMapping;
import com.khamitov.tgproject.model.constant.InlineButtonCallbacks;
import com.khamitov.tgproject.model.constant.TextConstants;
import com.khamitov.tgproject.model.dto.TelegramMessageDto;
import com.khamitov.tgproject.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.tgproject.model.constant.ActionsEnum;
import com.khamitov.tgproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoHandler {

    private final ImageRepository imageRepository;
    private final TelegramMessageMapping telegramMessageMapping;

    public TelegramMessageDto handleCommands(Update update, File file) {
        TelegramMessageDto message = telegramMessageMapping.fromMessage(update);
        message.setAction(ActionsEnum.SEND_MESSAGE);
        try {
            imageRepository.saveImage(message.getChatId(), file.getAbsolutePath());
            message.setText(TextConstants.IMAGE_SAVED);
            TelegramMessageKeyboardDto button = TelegramMessageKeyboardDto.builder()
                    .text(InlineButtonCallbacks.SHOW_IMAGES.getText())
                    .callback(InlineButtonCallbacks.SHOW_IMAGES.getCallback())
                    .build();
            message.setInlineKeyboard(List.of(List.of(button)));
            return message;
        } catch (Exception e) {
            log.error(e.getMessage());
            message.setText(e.getMessage());
            return message;
        }
    }
}
