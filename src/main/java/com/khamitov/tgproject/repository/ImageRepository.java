package com.khamitov.tgproject.repository;

import java.util.List;
import java.util.UUID;

public interface ImageRepository {

    void saveImage(Long id, String path);

    List<UUID> getImages(Long id);

    String getImage(UUID uuid);
}
