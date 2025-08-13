package com.voiceray.Voiceray.model;

public class AudioMetadataWithImage {
    private AudioMetadata metadata;
    private byte[] imageData;
    private String imageMimeType;

    public AudioMetadataWithImage(AudioMetadata metadata, byte[] imageData, String imageMimeType) {
        this.metadata = metadata;
        this.imageData = imageData;
        this.imageMimeType = imageMimeType;
    }

    public AudioMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(AudioMetadata metadata) {
        this.metadata = metadata;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageMimeType() {
        return imageMimeType;
    }

    public void setImageMimeType(String imageMimeType) {
        this.imageMimeType = imageMimeType;
    }
}
