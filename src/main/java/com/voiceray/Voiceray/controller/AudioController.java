package com.voiceray.Voiceray.controller;

import com.voiceray.Voiceray.model.*;
import com.voiceray.Voiceray.repository.AudioFileRepository;
import com.voiceray.Voiceray.repository.AudioMetadataRepository;
import com.voiceray.Voiceray.repository.ImageRepository;
import com.voiceray.Voiceray.service.AudioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final AudioMetadataRepository metadataRepository;
    private final ImageRepository imageRepository;
    private final AudioFileRepository audioFileRepository;
    private final AudioService audioService;

    public AudioController(AudioMetadataRepository metadataRepository,
                           ImageRepository imageRepository,
                           AudioFileRepository audioFileRepository, AudioService audioService) {
        this.metadataRepository = metadataRepository;
        this.imageRepository = imageRepository;
        this.audioFileRepository = audioFileRepository;
        this.audioService = audioService;
    }

    @PostMapping("save")
    public ResponseEntity<String> saveMetaData(@RequestBody AudioUploadRequest request) throws SQLException {
        String id = UUID.randomUUID().toString();

        AudioMetadata audioMetadata = new AudioMetadata(
                id,
                request.getTopic(),
                request.getDescription(),
                request.getSpeakerName(),
                request.getTags(),
                LocalDateTime.now(),
                null,
                null
                );
        metadataRepository.saveMetadata(audioMetadata);

        imageRepository.saveImage(id, request.getImageData(),request.getImageMimeType());

        audioFileRepository.saveAudio(id, request.getAudioData());

        return ResponseEntity.ok(id);
    }

    @GetMapping("/allMetadata")
    public List<AudioMetadataWithImage> getAllMetadataWithImageMetadata() {
        return audioService.getAllMetadataWithImage();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudioMetadata> getAudio(@PathVariable String id) {
        return ResponseEntity.ok(metadataRepository.findById(id));
    }
}
