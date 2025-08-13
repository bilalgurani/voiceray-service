package com.voiceray.Voiceray.repository;

import com.voiceray.Voiceray.model.ImageData;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ImageRepository {
    void saveImage(String id, byte[] imageData, String mimeType);
    Optional<ImageData> findImageById(String id);
}
