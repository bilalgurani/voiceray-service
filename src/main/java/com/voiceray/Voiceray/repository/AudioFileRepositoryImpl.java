package com.voiceray.Voiceray.repository;

import com.voiceray.Voiceray.model.AudioData;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AudioFileRepositoryImpl implements AudioFileRepository {
    private final JdbcTemplate jdbcTemplate;

    public AudioFileRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AudioData> rowMapper = ((rs, rowNum) ->
            new AudioData(
                    rs.getString("id"),
                    rs.getBytes("audio_bytes")
            ));

    @Override
    public void saveAudio(String id, byte[] audioData) {
        String sql = "INSERT INTO audio_data(id, audio_bytes) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, audioData);
    }

    @Override
    public Optional<AudioData> findAudioById(String id) {
        String sql = "SELECT * FROM audio_data WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
