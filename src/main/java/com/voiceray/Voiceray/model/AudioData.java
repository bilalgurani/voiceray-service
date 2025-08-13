package com.voiceray.Voiceray.model;

public class AudioData {
    private final String id;
    private byte[] data;

    public AudioData(String id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
