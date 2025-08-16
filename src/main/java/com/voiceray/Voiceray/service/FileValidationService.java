package com.voiceray.Voiceray.service;

import com.voiceray.Voiceray.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileValidationService {
    public static final String ERR_FILE_REQUIRED = "FILE_REQUIRED";
    public static final String ERR_FILE_SIZE_EXCEEDED = "FILE_SIZE_EXCEEDED";
    public static final String ERR_INVALID_FILE_TYPE = "INVALID_FILE_TYPE";
    public static final String ERR_INVALID_EXTENSION = "INVALID_FILE_EXTENSION";

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp");


    public void validateAudioFile(MultipartFile file) throws InvalidFileException {
        validateFile(file, "audioFile", "audio", 100 * 1024 * 1024, List.of(
                "audio/mpeg", "audio/mp3", "audio/wav", "audio/ogg"
        ));
    }

    public void validateImageFile(MultipartFile file) throws InvalidFileException {
        validateFile(file, "imageFile", "image", MAX_IMAGE_SIZE, ALLOWED_IMAGE_TYPES);
    }

    private void validateFile(MultipartFile file, String fieldName, String fileType,
                              long maxSize, List<String> allowedTypes)
            throws InvalidFileException {

        // 1. Check if file exists
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException(
                    fileType + " file is required",
                    fieldName,
                    ERR_FILE_REQUIRED
            );
        }

        // 2. Check file size
        if (file.getSize() > maxSize) {
            throw new InvalidFileException(
                    fileType + " file exceeds maximum size of " + formatSize(maxSize),
                    fieldName,
                    ERR_FILE_SIZE_EXCEEDED
            );
        }

        // 3. Check content type
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new InvalidFileException(
                    "Invalid " + fileType + " format. Supported types: " + allowedTypes,
                    fieldName,
                    ERR_INVALID_FILE_TYPE
            );
        }

        // 4. Check file extension
        String filename = file.getOriginalFilename();
        if (filename != null) {
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            if (!isValidExtension(extension, allowedTypes)) {
                throw new InvalidFileException(
                        "Invalid file extension. Supported: " + getSupportedExtensions(allowedTypes),
                        fieldName,
                        ERR_INVALID_EXTENSION
                );
            }
        }
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " bytes";
        if (bytes < 1024 * 1024) return (bytes / 1024) + "KB";
        return (bytes / (1024 * 1024)) + "MB";
    }

    private boolean isValidExtension(String extension, List<String> mimeTypes) {
        return mimeTypes.stream()
                .anyMatch(mime -> extension.equals(getExtensionFromMime(mime)));
    }

    private String getSupportedExtensions(List<String> mimeTypes) {
        return mimeTypes.stream()
                .map(this::getExtensionFromMime)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private String getExtensionFromMime(String mimeType) {
        return switch (mimeType) {
            case "audio/mpeg", "audio/mp3" -> "mp3";
            case "audio/wav" -> "wav";
            case "audio/ogg" -> "ogg";
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> "";
        };
    }

}
