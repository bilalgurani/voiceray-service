package com.voiceray.Voiceray.model;

import java.time.LocalDateTime;

public class AudioMetadata {
    private final String id;
    private String topic;
    private String description;
    private String speakerName;
    private String[] tags;
    private LocalDateTime uploadDateTime;
    private String thumbnailUrl;
    private String audioUrl;

    public AudioMetadata(String id, String topic, String description, String speakerName, String[] tags, LocalDateTime uploadDateTime, String thumbnailUrl, String audioUrl) {
        this.id = id;
        this.topic = topic;
        this.description = description;
        this.speakerName = speakerName;
        this.tags = tags;
        this.uploadDateTime = uploadDateTime;
        this.thumbnailUrl = thumbnailUrl;
        this.audioUrl = audioUrl;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public LocalDateTime getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(LocalDateTime uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
