package com.voiceray.Voiceray.repository;

import com.voiceray.Voiceray.model.AudioMetadata;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository {
    void saveAudio(String id, byte[] audioData);
    AudioMetadata findAudioById(String id);
}
