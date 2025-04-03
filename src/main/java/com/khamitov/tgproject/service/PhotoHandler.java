package com.khamitov.tgproject.service;

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

    public TelegramMessageDto handleCommands(TelegramMessageDto message, File file) {
        Long chatId = message.getChatId();
        try {
            imageRepository.saveImage(chatId, file.getAbsolutePath());
            TelegramMessageKeyboardDto button = TelegramMessageKeyboardDto.builder()
                    .text(InlineButtonCallbacks.SHOW_IMAGES.getText())
                    .callback(InlineButtonCallbacks.SHOW_IMAGES.getCallback())
                    .build();
            return TelegramMessageDto.builder()
                    .chatId(chatId)
                    .action(ActionsEnum.SEND_MESSAGE)
                    .text(TextConstants.IMAGE_SAVED)
                    .inlineKeyboard(List.of(List.of(button)))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return TelegramMessageDto.builder()
                    .chatId(chatId)
                    .action(ActionsEnum.SEND_MESSAGE)
                    .text(e.getMessage())
                    .build();
        }
    }
}
