package com.voiceray.Voiceray.repository;

import com.voiceray.Voiceray.model.AudioMetadata;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface AudioMetadataRepository {
    List<AudioMetadata> getAllMetadata();
    AudioMetadata findById(String id);
    void saveMetadata(AudioMetadata metadata) throws SQLException;
}
