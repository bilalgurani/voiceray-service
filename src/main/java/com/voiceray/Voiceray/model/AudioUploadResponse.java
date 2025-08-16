package com.voiceray.Voiceray.model;

public class AudioUploadResponse {
    private final String id;
    private final String message;

    public AudioUploadResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
