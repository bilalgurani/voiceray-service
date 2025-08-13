package com.voiceray.Voiceray.repository;

import com.voiceray.Voiceray.model.ImageData;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ImageRepositoryImpl implements ImageRepository {

    private final JdbcTemplate jdbcTemplate;

    public ImageRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ImageData> rowMapper = ((rs, rowNum) ->
            new ImageData(
            rs.getString("id"),
            rs.getString("image_bytes").getBytes(),
            rs.getString("mime_type")
    ));

    @Override
    public void saveImage(String id, byte[] imageData, String mimeType) {
        String sql = "INSERT INTO image_data(id, image_bytes, mime_type) VALUES (?, ?, ?)";
        jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<ImageData> findImageById(String id) {
        String sql = "SELECT * FROM image_data WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
