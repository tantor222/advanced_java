package com.khamitov.telegram.repository;

public interface TelegramContextRepository {

    void setContext(Long chatId, String prefix);

    String getContext(Long chatId);

    void removeContext(Long chatId);
}
