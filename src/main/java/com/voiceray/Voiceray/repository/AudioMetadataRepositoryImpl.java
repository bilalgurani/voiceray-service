package com.voiceray.Voiceray.repository;

import com.voiceray.Voiceray.model.AudioMetadata;
import com.voiceray.Voiceray.model.AudioMetadataWithImage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AudioMetadataRepositoryImpl implements AudioMetadataRepository {
    private final JdbcTemplate jdbcTemplate;

    public AudioMetadataRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AudioMetadata> rowMapper = (rs, rowNum) -> {
        String[] tags = convertArray(rs.getArray("tags"));

        return new AudioMetadata(
                rs.getString("id"),
                rs.getString("topic"),
                rs.getString("description"),
                rs.getString("speaker_name"),
                tags,
                rs.getTimestamp("upload_date").toLocalDateTime()
        );
    };

    private String[] convertArray(Array sqlArray) throws SQLException {
        return sqlArray != null ? (String[]) sqlArray.getArray() : null;
    }

    @Override
    public List<AudioMetadata> getAllMetadata() {
        String sql = "SELECT * FROM audio_metadata";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<AudioMetadata> findById(String id) {
        String sql = "SELECT * FROM audio_metadata WHERE id = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, rowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveMetadata(AudioMetadata metadata) throws SQLException {
        String sql = "INSERT INTO audio_metadata(id, topic, description, speakerName, tags, uploadDateTime, imageId) VALUES (?, ?, ?, ?, ?, ?)";
        assert jdbcTemplate.getDataSource() != null;
        jdbcTemplate.update(
                sql,
                metadata.getId(),
                metadata.getTopic(),
                metadata.getDescription(),
                metadata.getSpeakerName(),
                jdbcTemplate.getDataSource().getConnection().createArrayOf("varchar", metadata.getTags()),
                metadata.getUploadDateTime()
                );
    }

}
