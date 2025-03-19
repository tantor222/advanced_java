package com.khamitov.tgproject.service;

import com.khamitov.tgproject.model.constant.InlineButtonCallbacks;
import com.khamitov.tgproject.model.constant.TextConstants;
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
    private final Map<String, Function<TelegramMessageDto, TelegramMessageDto>> consumers = new HashMap<>();
    private final ImageService imageService;

    @PostConstruct
    public void init() {
        consumers.put(InlineButtonCallbacks.SHOW_IMAGES.getCallback(), this::nextImage);
        consumers.put(InlineButtonCallbacks.NEXT_IMAGE.getCallback(), this::nextImage);
        consumers.put(InlineButtonCallbacks.GO_BACK.getCallback(), this::goBack);
    }

    public TelegramMessageDto handleCommands(TelegramMessageDto message) {
        try {
            String[] directionLine = message.getCallback().split(DIRECTION_SEP);
            return Optional.ofNullable(consumers.get(directionLine[0]))
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
        UUID image = imageService.getNextImage(message.getChatId(), direction);
        if (image == null) {
            var button = TelegramMessageKeyboardDto.builder()
                    .text(InlineButtonCallbacks.SHOW_IMAGES.getText())
                    .callback(InlineButtonCallbacks.SHOW_IMAGES.getCallback())
                    .build();
            return TelegramMessageDto.builder()
                    .text(TextConstants.IMAGE_NOT_FOUND)
                    .action(ActionsEnum.SEND_MESSAGE)
                    .chatId(message.getChatId())
                    .inlineKeyboard(List.of(List.of(button)))
                    .build();
        }
        String file = imageService.getImagePath(image);
        TelegramMessageKeyboardDto buttonNext = TelegramMessageKeyboardDto.builder()
                .callback(InlineButtonCallbacks.NEXT_IMAGE.getCallback() + DIRECTION_SEP + image)
                .text(InlineButtonCallbacks.NEXT_IMAGE.getText())
                .build();
        TelegramMessageKeyboardDto buttonBack = TelegramMessageKeyboardDto.builder()
                .callback(InlineButtonCallbacks.GO_BACK.getCallback())
                .text(InlineButtonCallbacks.GO_BACK.getText())
                .build();
        return TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_PHOTO)
                .attachment(file)
                .chatId(message.getChatId())
                .inlineKeyboard(List.of(
                        List.of(buttonNext),
                        List.of(buttonBack)
                ))
                .build();
    }

    private TelegramMessageDto goBack(TelegramMessageDto message) {
        TelegramMessageKeyboardDto button = TelegramMessageKeyboardDto.builder()
                .text(InlineButtonCallbacks.SHOW_IMAGES.getText())
                .callback(InlineButtonCallbacks.SHOW_IMAGES.getCallback())
                .build();
        return TelegramMessageDto.builder()
                .action(ActionsEnum.SEND_MESSAGE)
                .text(TextConstants.SELECT_ACTION)
                .chatId(message.getChatId())
                .inlineKeyboard(List.of(List.of(button)))
                .build();
    }
}
