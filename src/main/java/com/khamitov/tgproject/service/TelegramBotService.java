package com.khamitov.tgproject.service;

import com.khamitov.tgproject.mapping.TelegramMessageMapping;
import com.khamitov.tgproject.model.dto.TelegramMessageDto;
import com.khamitov.tgproject.model.dto.TelegramMessageKeyboardDto;
import com.khamitov.tgproject.model.constant.ActionsEnum;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class TelegramBotService extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private static final String MARKDOWN_MODE = "MarkdownV2";
    private final Map<ActionsEnum, Consumer<TelegramMessageDto>> consumers = new EnumMap<>(ActionsEnum.class);

    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final PhotoHandler photoHandler;
    private final TelegramMessageMapping telegramMessageMapping;

    public TelegramBotService(@Value("${bot.name}") String botUsername,
                              @Value("${bot.token}") String botToken,
                              MessageHandler messageHandler,
                              CallbackHandler callbackHandler,
                              PhotoHandler photoHandler,
                              TelegramMessageMapping telegramMessageMapping) {
        super(botToken);
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
        this.photoHandler = photoHandler;
        this.telegramMessageMapping = telegramMessageMapping;
    }

    @PostConstruct
    public void init() {
        consumers.put(ActionsEnum.SEND_MESSAGE, this::sendMessage);
        consumers.put(ActionsEnum.SEND_PHOTO, this::sendPhoto);
        consumers.put(ActionsEnum.EDIT_MEDIA_CAPTION, this::editMediaCaption);
        consumers.put(ActionsEnum.EDIT_MEDIA, this::editMedia);
        consumers.put(ActionsEnum.EDIT_MESSAGE_REPLY_MARKUP, this::editMessageReplyMarkup);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            handlePhoto(update);
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        } else {
            log.error("UNKNOWN_BOT_CALL_MESSAGE: {}", update);
        }
    }

    private void handleMessage(Update update) {
        TelegramMessageDto message = telegramMessageMapping.fromMessage(update);
        TelegramMessageDto response = messageHandler.handleCommands(message);
        consumers.get(response.getAction()).accept(response);
    }

    private void handlePhoto(Update update) {
        Optional<PhotoSize> photo = update.getMessage()
                .getPhoto()
                .stream()
                .max(Comparator.comparing(PhotoSize::getFileSize));
        if (photo.isPresent()) {
            try {
                var getFile = new GetFile();
                getFile.setFileId(photo.get().getFileId());
                var file = execute(getFile);
                File photoFile = downloadFile(file);
                TelegramMessageDto response = photoHandler.handleCommands(update, photoFile);
                consumers.get(response.getAction()).accept(response);
            } catch (TelegramApiException err) {
                log.error("", err);
                sendErrorMessage(update, err.getMessage());
            }
        }
    }

    private void handleCallback(Update update) {
        TelegramMessageDto message = telegramMessageMapping.fromCallback(update);
        TelegramMessageDto response = callbackHandler.handleCommands(message);
        consumers.get(response.getAction()).accept(response);
    }

    private void sendErrorMessage(Update update, String msg) {
        SendMessage response = new SendMessage();
        response.setChatId(update.getMessage().getChatId());
        response.setText(msg);
        sendMessage(response);
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void sendMessage(Method sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Поменять клавиатуру сообщения
     *
     * @param message {@link TelegramMessageDto}
     */
    private void editMessageReplyMarkup(TelegramMessageDto message) {
        List<List<TelegramMessageKeyboardDto>> inlineKeyboard = message.getInlineKeyboard();
        var messageText = new EditMessageReplyMarkup();

        messageText.setMessageId(Integer.valueOf(message.getMessageId()));
        messageText.setChatId(message.getChatId());

        // при изменении сообщения кнопки только самого сообщения
        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        }
        sendMessage(messageText);
    }

    /**
     * Отправить сообщение без картинки
     */
    private void sendMessage(TelegramMessageDto message) {
        List<List<TelegramMessageKeyboardDto>> inlineKeyboard = message.getInlineKeyboard();
        List<List<TelegramMessageKeyboardDto>> replyKeyboard = message.getReplyKeyboard();
        var messageText = new SendMessage();

        messageText.setParseMode(MARKDOWN_MODE);
        messageText.setChatId(message.getChatId());
        messageText.setText(message.getText());

        // тут кнопки на выбор, можно как сообщение с кнопками, а можно и клавиатуру
        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        } else if (replyKeyboard != null) {
            // empty keyboard -> do remove
            if (replyKeyboard.isEmpty()) {
                messageText.setReplyMarkup(new ReplyKeyboardRemove(true));
            } else {
                var keyboard = getReplyKeyboard(replyKeyboard);
                messageText.setReplyMarkup(keyboard);
            }
        }
        sendMessage(messageText);
    }

    /**
     * Отправить картинку с текстом
     */
    private void sendPhoto(TelegramMessageDto message) {
        var inlineKeyboard = message.getInlineKeyboard();
        var replyKeyboard = message.getReplyKeyboard();
        var messageText = new SendPhoto();
        try {
            messageText.setParseMode(MARKDOWN_MODE);
            Path path = Paths.get(message.getAttachment());
            messageText.setPhoto(new InputFile(Files.newInputStream(path), message.getAttachment()));
            messageText.setChatId(message.getChatId());
            messageText.setCaption(message.getText());

            // тут кнопки на выбор, можно как сообщение с кнопками, а можно и клавиатуру
            if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
                var keyboard = getInlineKeyboard(inlineKeyboard);
                messageText.setReplyMarkup(keyboard);
            } else if (replyKeyboard != null) {
                if (replyKeyboard.isEmpty()) {
                    // empty keyboard -> do remove
                    messageText.setReplyMarkup(new ReplyKeyboardRemove(true));
                } else {
                    var keyboard = getReplyKeyboard(replyKeyboard);
                    messageText.setReplyMarkup(keyboard);
                }
            }
            execute(messageText);
        } catch (IOException | TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Изменить текст сообщения с картинкой (+ клавиатуру)
     */
    private void editMediaCaption(TelegramMessageDto message) {
        var inlineKeyboard = message.getInlineKeyboard();
        var messageText = new EditMessageCaption();

        messageText.setParseMode(MARKDOWN_MODE);
        messageText.setMessageId(Integer.valueOf(message.getMessageId()));
        messageText.setChatId(message.getChatId());
        messageText.setCaption(message.getText());

        // при изменении сообщения кнопки только самого сообщения
        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        }
        sendMessage(messageText);
    }

    /**
     * Изменить текст и картинку сообщения с картинкой (+ клавиатуру)
     */
    private void editMedia(TelegramMessageDto message) {
        var inlineKeyboard = message.getInlineKeyboard();
        var messageText = new EditMessageMedia();
        var photo = new InputMediaPhoto(message.getAttachment());
        photo.setCaption(message.getText());
        photo.setParseMode(MARKDOWN_MODE);

        messageText.setMessageId(Integer.valueOf(message.getMessageId()));
        messageText.setChatId(message.getChatId());
        messageText.setMedia(photo);

        // при изменении сообщения кнопки только самого сообщения
        if (inlineKeyboard != null && !inlineKeyboard.isEmpty()) {
            var keyboard = getInlineKeyboard(inlineKeyboard);
            messageText.setReplyMarkup(keyboard);
        }
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private InlineKeyboardMarkup getInlineKeyboard(
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
            for (TelegramMessageKeyboardDto button : row) {
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

