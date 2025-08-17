package com.voiceray.Voiceray.service;

import com.voiceray.Voiceray.model.AudioMetadata;
import com.voiceray.Voiceray.model.AudioMetadataWithImage;
import com.voiceray.Voiceray.model.AudioUploadRequest;
import com.voiceray.Voiceray.model.ImageData;
import com.voiceray.Voiceray.repository.AudioMetadataRepository;
import com.voiceray.Voiceray.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AudioService {
    private final AudioMetadataRepository audioMetadataRepository;
    private final ImageRepository imageRepository;
    private final BackblazeService backblazeService;
    private final FileValidationService fileValidationService;

    @Value("${backblaze.audio.bucketId}")
    private String audioBucketId;

    @Value("${backblaze.image.bucketId}")
    private String imageBucketId;

    @Value("${backblaze.audio.bucketName}")
    private String audioBucketName;

    @Value("${backblaze.image.bucketName}")
    private String imageBucketName;

    public AudioService(AudioMetadataRepository audioMetadataRepository, ImageRepository imageRepository, BackblazeService backblazeService, FileValidationService fileValidationService) {
        this.audioMetadataRepository = audioMetadataRepository;
        this.imageRepository = imageRepository;
        this.backblazeService = backblazeService;
        this.fileValidationService = fileValidationService;
    }

    public String processAudioUpload(
            AudioUploadRequest request,
            MultipartFile audioFile,
            MultipartFile thumbnail) throws IOException, SQLException {
        String id =  UUID.randomUUID().toString();
        // 1. Validate files
        fileValidationService.validateAudioFile(audioFile);
        fileValidationService.validateImageFile(thumbnail);

        // 2. Upload files to Backblaze
        String audioUrl = backblazeService.uploadFile(audioFile, audioBucketName, audioBucketId);
        String thumbnailUrl = backblazeService.uploadFile(thumbnail, imageBucketName, imageBucketId);

        // 3. Create metadata entity
        AudioMetadata metadata = new AudioMetadata(
                id,
                request.getTopic(),
                request.getDescription(),
                request.getSpeakerName(),
                request.getTags(),
                LocalDateTime.now(),
                thumbnailUrl,
                audioUrl
        );

        audioMetadataRepository.saveMetadata(metadata);
        return id;
    }

    public List<AudioMetadata> getAllData() {
        return audioMetadataRepository.getAllData();
    }
}
