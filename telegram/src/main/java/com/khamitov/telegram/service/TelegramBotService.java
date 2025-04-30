package com.khamitov.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final String botUsername;
    private final HandleReceive handleReceive;


    public TelegramBotService(@Value("${bot.name}") String botUsername,
                              @Value("${bot.token}") String botToken,
                              HandleReceive handleReceive) {
        super(botToken);
        this.botUsername = botUsername;
        this.handleReceive = handleReceive;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleReceive.handleMessage(update);
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            handlePhoto(update);
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        } else {
            log.error("Неизвестная команда: {}", update);
        }
    }

    private void handleCallback(Update update) {
        handleReceive.handleCallback(update);
        var clear = new EditMessageReplyMarkup();
        clear.setChatId(update.getCallbackQuery().getFrom().getId());
        clear.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        sendMessage(clear);
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
                handleReceive.handlePhoto(update, photoFile);
            } catch (TelegramApiException err) {
                log.error("", err);
                sendErrorMessage(update, err.getMessage());
            }
        }
    }


    public void sendErrorMessage(Update update, String msg) {
        SendMessage response = new SendMessage();
        response.setChatId(update.getMessage().getChatId());
        response.setText(msg);
        sendMessage(response);
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> void sendMessage(Method sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

