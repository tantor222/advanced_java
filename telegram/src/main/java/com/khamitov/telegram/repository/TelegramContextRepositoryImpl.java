package com.khamitov.telegram.repository;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TelegramContextRepositoryImpl implements TelegramContextRepository {

    private final ConcurrentHashMap<Long, String> cache = new ConcurrentHashMap<Long, String>();

    @Override
    public void setContext(Long chatId, String prefix) {
        cache.put(chatId, prefix);
    }

    @Override
    public String getContext(Long chatId) {
        return cache.get(chatId);
    }

    @Override
    public void removeContext(Long chatId) {
        cache.remove(chatId);
    }
}
