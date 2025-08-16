package com.voiceray.Voiceray.controller;

import com.voiceray.Voiceray.exception.InvalidFileException;
import com.voiceray.Voiceray.model.*;
import com.voiceray.Voiceray.repository.AudioMetadataRepository;
import com.voiceray.Voiceray.service.AudioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final AudioMetadataRepository metadataRepository;
    private final AudioService audioService;

    public AudioController(AudioMetadataRepository metadataRepository,
                           AudioService audioService) {
        this.metadataRepository = metadataRepository;
        this.audioService = audioService;
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AudioUploadResponse> saveMetaData(
            @RequestPart("metadata") AudioUploadRequest request,
            @RequestPart("audioFile") MultipartFile audioFile,
            @RequestPart("thumbnail") MultipartFile thumbnail) throws SQLException {
        String audioId;

        try {
            audioId = audioService.processAudioUpload(request, audioFile, thumbnail);
            return ResponseEntity.ok(new AudioUploadResponse(audioId, "Upload successful"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AudioUploadResponse(null, "File upload failed: " + e.getMessage()));
        } catch (InvalidFileException e) {
            return ResponseEntity.badRequest()
                    .body(new AudioUploadResponse(null, e.getMessage()));
        }
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
