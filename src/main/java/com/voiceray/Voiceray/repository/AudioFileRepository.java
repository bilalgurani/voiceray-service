package com.voiceray.Voiceray.repository;

import com.voiceray.Voiceray.model.AudioData;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AudioFileRepository {
    void saveAudio(String id, byte[] audioData);
    Optional<AudioData> findAudioById(String id);
}
