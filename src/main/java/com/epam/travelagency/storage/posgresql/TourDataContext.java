package com.epam.travelagency.storage.posgresql;

import com.epam.travelagency.bean.Tour;
import com.epam.travelagency.storage.DataContext;
import com.epam.travelagency.storage.exception.NullGeneratedKeyException;
import com.epam.travelagency.storage.mapper.TourRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

public class TourDataContext implements DataContext<Tour> {

    private static Logger LOG = LoggerFactory.getLogger(TourDataContext.class);

    private static final String SELECT_FROM_TOUR =
            "SELECT id, photo, date, duration, description," +
                    " cost, tour_type, hotel_id, country_id FROM tour";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TourDataContext(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer create(Tour entity) {
        KeyHolder generatedIdHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO tour (photo, date, duration," +
                " description, cost, tour_type, hotel_id, country_id)" +
                " VALUES (?,?::DATE,?,?,?,?::tour_type,?,?)";
        jdbcTemplate.update(connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, entity.getPhoto());
                    preparedStatement.setString(2, entity.getDate());
                    preparedStatement.setInt(3, entity.getDuration());
                    preparedStatement.setString(4, entity.getDescription());
                    preparedStatement.setBigDecimal(5, entity.getCost());
                    preparedStatement.setString(6, entity.getTourType().name().toLowerCase());
                    preparedStatement.setInt(7, entity.getHotelId());
                    preparedStatement.setInt(8, entity.getCountryId());
                    return preparedStatement;
                },
                generatedIdHolder);
        LOG.info(String.format("Tour [%s] to country {id=%d} and with hotel {id=%d} was created in database",
                entity.getTourType(), entity.getCountryId(), entity.getHotelId()));

        try {
            return generatedIdHolder.getKey().intValue();
        } catch (NullPointerException e) {
            throw new NullGeneratedKeyException(e);
        }
    }

    @Override
    public Tour read(Integer id) {
        return jdbcTemplate.queryForObject(
                SELECT_FROM_TOUR + " WHERE id=?",
                new TourRowMapper(), id);
    }

    @Override
    public void update(Tour entity) {
        jdbcTemplate.update("UPDATE tour" +
                        " SET photo=?, date=?, duration=?," +
                        " description=?, cost=?, tour_type=?" +
                        " WHERE id=?",
                preparedStatement -> {
                    preparedStatement.setString(1, entity.getPhoto());
                    preparedStatement.setObject(2, entity.getDate(), Types.DATE);
                    preparedStatement.setInt(3, entity.getDuration());
                    preparedStatement.setString(4, entity.getDescription());
                    preparedStatement.setBigDecimal(5, entity.getCost());
                    preparedStatement.setString(6, entity.getTourType().name().toLowerCase());
                }
        );
        LOG.info(String.format("Tour [%s] to country {id=%d} and with hotel {id=%d} was updated in database",
                entity.getTourType(), entity.getCountryId(), entity.getHotelId()));
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM tour WHERE id=?", id);
        LOG.info(String.format("Tour with id '%d' was deleted from database", id));
    }

    @Override
    public List<Tour> read() {
        return jdbcTemplate.query(SELECT_FROM_TOUR, new TourRowMapper());
    }
}
