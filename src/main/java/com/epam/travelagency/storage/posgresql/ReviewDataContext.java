package com.epam.travelagency.storage.posgresql;

import com.epam.travelagency.bean.Review;
import com.epam.travelagency.storage.DataContext;
import com.epam.travelagency.storage.exception.NullGeneratedKeyException;
import com.epam.travelagency.storage.mapper.ReviewRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class ReviewDataContext implements DataContext<Review> {

    private static Logger LOG = LoggerFactory.getLogger(ReviewDataContext.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDataContext(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer create(Review entity) {
        KeyHolder generatedIdHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO review (date, text, user_id, tour_id) VALUES (?::DATE,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, entity.getDate());
            preparedStatement.setString(2, entity.getText());
            preparedStatement.setInt(3, entity.getUserId());
            preparedStatement.setInt(4, entity.getTourId());
            return preparedStatement;
        }, generatedIdHolder);
        LOG.info(String.format("Review from user 'id=%d' for tour 'id=%d' was created in database",
                entity.getUserId(), entity.getTourId()));
        try {
            return generatedIdHolder.getKey().intValue();
        } catch (NullPointerException e) {
            throw new NullGeneratedKeyException(e);
        }
    }

    @Override
    public Review read(Integer id) {
        return jdbcTemplate.queryForObject("SELECT id, date, text, user_id, tour_id" +
                " FROM review" +
                " WHERE id=?", new ReviewRowMapper(), id);
    }

    @Override
    public void update(Review entity) {
        jdbcTemplate.update("UPDATE review" +
                        " SET date=?::DATE, text=?, user_id=?, tour_id=?" +
                        " WHERE id=?",
                preparedStatement -> {
                    preparedStatement.setString(1, entity.getDate());
                    preparedStatement.setString(2, entity.getText());
                    preparedStatement.setInt(3, entity.getUserId());
                    preparedStatement.setInt(4, entity.getTourId());
                    preparedStatement.setInt(5, entity.getId());
                });
        LOG.info(String.format("Review from user 'id=%d' for tour 'id=%d' was updated in database",
                entity.getUserId(), entity.getTourId()));
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM review where id=?", id);
        LOG.info(String.format("Review with id '%d' was deleted from database", id));
    }

    @Override
    public List<Review> read() {
        return jdbcTemplate.query("SELECT id, date, text, user_id, tour_id" +
                " FROM review", new ReviewRowMapper());
    }
}
