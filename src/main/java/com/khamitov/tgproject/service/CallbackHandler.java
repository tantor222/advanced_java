package com.khamitov.tgproject.service;

import com.khamitov.tgproject.model.constant.InlineButtons;
import com.khamitov.tgproject.model.dto.TelegramMessageDto;
import com.khamitov.tgproject.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.tgproject.model.constant.ActionsEnum;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackHandler {

    private static final String DIRECTION_SEP = ":";
    private final Map<String, Function<TelegramMessageDto, TelegramMessageDto>> strategies = new HashMap<>();
    private final ImageService imageService;

    @PostConstruct
    @SuppressWarnings("unused")
    public void init() {
        strategies.put(InlineButtons.SHOW_IMAGES, this::nextImage);
        strategies.put(InlineButtons.NEXT_IMAGE, this::nextImage);
        strategies.put(InlineButtons.GO_BACK, this::goBack);
    }

    public TelegramMessageDto handleCommands(TelegramMessageDto message) {
        try {
            String[] directionLine = message.getCallback().split(DIRECTION_SEP);
            return Optional.ofNullable(strategies.get(directionLine[0]))
                    .map(fn -> fn.apply(message))
                    .orElse(null);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage());
            return null;
        }
    }

    private TelegramMessageDto nextImage(TelegramMessageDto message) {
        String[] directionLine = message.getCallback().split(DIRECTION_SEP);
        UUID direction = directionLine.length > 1 ? UUID.fromString(directionLine[1]) : null;
        UUID image = imageService.getNextImage(message.getUserId(), direction);
        if (image == null) {
            message.setText("Картинка не найдена");
            message.setAction(ActionsEnum.SEND_MESSAGE);
            var button = TelegramMessageKeyboardDto.builder()
                    .text("Посмотреть мои картинки")
                    .callback(InlineButtons.SHOW_IMAGES)
                    .build();
            message.setInlineKeyboard(List.of(List.of(button)));
            return message;
        }
        String file = imageService.getImagePath(image);
        message.setAction(ActionsEnum.SEND_PHOTO);
        message.setAttachment(file);
        TelegramMessageKeyboardDto buttonNext = TelegramMessageKeyboardDto.builder()
                .callback(InlineButtons.NEXT_IMAGE + DIRECTION_SEP + image)
                .text("Далее")
                .build();
        TelegramMessageKeyboardDto buttonBack = TelegramMessageKeyboardDto.builder()
                .callback(InlineButtons.GO_BACK)
                .text("Назад")
                .build();
        message.setInlineKeyboard(List.of(
                List.of(buttonNext),
                List.of(buttonBack)
        ));
        return message;
    }

    private TelegramMessageDto goBack(TelegramMessageDto message) {
        message.setText("Выберете действие");
        TelegramMessageKeyboardDto button = TelegramMessageKeyboardDto.builder()
                .text("Посмотреть мои картинки")
                .callback(InlineButtons.SHOW_IMAGES)
                .build();
        message.setInlineKeyboard(List.of(List.of(button)));
        message.setAction(ActionsEnum.SEND_MESSAGE);
        return message;
    }
}
