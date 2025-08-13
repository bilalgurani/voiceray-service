package com.voiceray.Voiceray.service;

import com.voiceray.Voiceray.model.AudioMetadata;
import com.voiceray.Voiceray.model.AudioMetadataWithImage;
import com.voiceray.Voiceray.model.ImageData;
import com.voiceray.Voiceray.repository.AudioMetadataRepository;
import com.voiceray.Voiceray.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AudioService {
    private final AudioMetadataRepository audioMetadataRepository;
    private final ImageRepository imageRepository;

    public AudioService(AudioMetadataRepository audioMetadataRepository, ImageRepository imageRepository) {
        this.audioMetadataRepository = audioMetadataRepository;
        this.imageRepository = imageRepository;
    }

    public List<AudioMetadataWithImage> getAllMetadataWithImage() {
        return audioMetadataRepository.getAllMetadata().stream()
                .map(metadata -> {
                    ImageData imageData = imageRepository.findImageById(metadata.getId()).orElse(null);
                    return new AudioMetadataWithImage(
                            metadata,
                            imageData != null ? imageData.getImageData() : null,
                            imageData != null ? imageData.getMimeType() : null
                            );
        })
                .collect(Collectors.toList());
    }
}
