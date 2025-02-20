package com.khamitov.tgproject.service;

import com.khamitov.tgproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public UUID getNextImage(Long id, UUID uuid) {
        List<UUID> images = imageRepository.getImages(id);
        if (images.isEmpty()) {
            return null;
        }
        if (uuid == null || images.indexOf(uuid) == images.size() - 1) {
            return images.getFirst();
        }
        return images.get(images.indexOf(uuid) + 1);
    }

    public String getImagePath(UUID uuid) {
        return imageRepository.getImage(uuid);
    }
}
