package com.voiceray.Voiceray.model;

public class ImageData {
    private final String id;
    private byte[] imageData;
    private String mimeType; // e.g., "image/jpeg"

    public ImageData(String id, byte[] imageData, String mimeType) {
        this.id = id;
        this.imageData = imageData;
        this.mimeType = mimeType;
    }

    public String getId() {
        return id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
