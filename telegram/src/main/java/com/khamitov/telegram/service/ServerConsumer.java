package com.khamitov.telegram.service;

import com.khamitov.model.dto.ActionsEnum;
import com.khamitov.model.dto.TelegramMessageDto;
import com.khamitov.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.telegram.repository.TelegramContextRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerConsumer {
    
    private final Map<ActionsEnum, Consumer<TelegramMessageDto>> strategies = new EnumMap<>(ActionsEnum.class);
    private static final String ACTION_NOT_FOUND = "MQ_CONSUME_EMPTY_ACTION: {}";
    private static final String ACTION_UNKNOWN = "MQ_CONSUME_UNKNOWN_ACTION: {}";

    private final TelegramBotService telegramBot;
    private final TelegramContextRepository telegramContextRepository;

    @PostConstruct
    public void init() {
        strategies.put(ActionsEnum.SEND_MESSAGE, this::sendMessage);
        strategies.put(ActionsEnum.SEND_PHOTO, this::sendPhoto);
        strategies.put(ActionsEnum.EDIT_MESSAGE, this::editMessage);
        strategies.put(ActionsEnum.EDIT_MESSAGE_REPLY_MARKUP, this::editMessageReplyMarkup);
    }

    @RabbitListener(queues = "${spring.rabbitmq.server.out-queue}")
    public void receive(TelegramMessageDto message) {
        log.debug("[TELEGRAM:RabbitMQ] Consume message: {}", message);
        if (message.getAction() == null) {
            log.error(ACTION_NOT_FOUND, message);
        } else if (!strategies.containsKey(message.getAction())) {
            log.warn(ACTION_UNKNOWN, message);
        } else {
            try {
                if (message.getContext() != null) {
                    telegramContextRepository.setContext(message.getChatId(), message.getContext());
                }
                strategies.get(message.getAction()).accept(message);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private void editMessage(TelegramMessageDto message) {
        var inlineKeyboard = message.getInlineKeyboard();
        var messageText = new EditMessageText();

        messageText.setMessageId(Integer.valueOf(message.getMessageId()));
        messageText.setChatId(message.getChatId());
        messageText.setText(message.getText());

        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        }

        try {
            telegramBot.execute(messageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void editMessageReplyMarkup(TelegramMessageDto message) {
        var inlineKeyboard = message.getInlineKeyboard();
        var messageText = new EditMessageReplyMarkup();

        messageText.setMessageId(Integer.valueOf(message.getMessageId()));
        messageText.setChatId(message.getChatId());

        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        }

        try {
            telegramBot.execute(messageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendMessage(TelegramMessageDto message) {
        var inlineKeyboard = message.getInlineKeyboard();
        var replyKeyboard = message.getReplyKeyboard();
        var messageText = new SendMessage();

        messageText.setChatId(message.getChatId());
        messageText.setText(message.getText());

        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        } else if (replyKeyboard != null) {
            if (replyKeyboard.isEmpty()) {
                messageText.setReplyMarkup(new ReplyKeyboardRemove(true));
            } else {
                var keyboard = getReplyKeyboard(replyKeyboard);
                messageText.setReplyMarkup(keyboard);
            }
        }
        telegramBot.sendMessage(messageText);
    }

    private void sendPhoto(TelegramMessageDto message) {
        var inlineKeyboard = message.getInlineKeyboard();
        var replyKeyboard = message.getReplyKeyboard();
        var messageText = new SendPhoto();

        messageText.setPhoto(new InputFile(message.getAttachment()));
        messageText.setChatId(message.getChatId());
        messageText.setCaption(message.getText());

        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        } else if (replyKeyboard != null) {
            if (replyKeyboard.isEmpty()) {
                messageText.setReplyMarkup(new ReplyKeyboardRemove(true));
            } else {
                var keyboard = getReplyKeyboard(replyKeyboard);
                messageText.setReplyMarkup(keyboard);
            }
        }

        try {
            telegramBot.execute(messageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private static InlineKeyboardMarkup getInlineKeyboard(
            List<List<TelegramMessageKeyboardDto>> inlineKeyboard
    ) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (var row : inlineKeyboard) {
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            for (var button : row) {
                var keyboardButton = new InlineKeyboardButton(button.getText());
                if (Objects.nonNull(button.getCallback())) {
                    keyboardButton.setCallbackData(button.getCallback());
                }
                buttons.add(keyboardButton);
            }
            rows.add(buttons);
        }

        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getReplyKeyboard(
            List<List<TelegramMessageKeyboardDto>> replyKeyboard
    ) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (var row : replyKeyboard) {
            var buttons = new KeyboardRow();
            for (var button : row) {
                KeyboardButton keyboardButton = new KeyboardButton(button.getText());
                buttons.add(keyboardButton);
            }
            keyboard.add(buttons);

        }
        var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
