package com.khamitov.server.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ImageRepositoryImpl implements ImageRepository {

    private final Map<Long, List<UUID>> cache = new ConcurrentHashMap<>();
    private final Map<UUID, String> images = new ConcurrentHashMap<>();

    @Override
    public void saveImage(Long id, String path) {
        List<UUID> bucket = cache.computeIfAbsent(id, k -> new ArrayList<>());
        UUID uuid = UUID.randomUUID();
        images.put(uuid, path);
        bucket.add(uuid);
    }

    @Override
    public List<UUID> getImages(Long id) {
        if (!cache.containsKey(id)) {
            cache.put(id, new ArrayList<>());
        }
        return cache.get(id);
    }

    @Override
    public String getImage(UUID uuid) {
        return images.get(uuid);
    }
}
