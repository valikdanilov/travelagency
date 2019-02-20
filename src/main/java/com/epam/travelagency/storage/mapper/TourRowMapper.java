package com.epam.travelagency.storage.mapper;

import com.epam.travelagency.bean.Country;
import com.epam.travelagency.bean.Hotel;
import com.epam.travelagency.bean.Tour;
import com.epam.travelagency.bean.enumeration.HotelFeature;
import com.epam.travelagency.bean.enumeration.TourType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TourRowMapper implements RowMapper<Tour> {
    @Override
    public Tour mapRow(ResultSet resultSet, int i) throws SQLException {
        Tour tour = new Tour();
        tour.setId(resultSet.getInt("tour_id"));
        tour.setPhoto(resultSet.getString("photo"));
        tour.setDate(resultSet.getString("date"));
        tour.setDuration(resultSet.getInt("duration"));
        tour.setDescription(resultSet.getString("description"));
        tour.setCost(resultSet.getBigDecimal("cost"));
        tour.setTourType(TourType.valueOf(
                resultSet.getString("tour_type")
                        .toUpperCase()));
        Country country = new Country();
        country.setId(resultSet.getInt("country_id"));
        country.setName(resultSet.getString("country_name"));
        Hotel hotel = new Hotel();
        hotel.setId(resultSet.getInt("hotel_id"));
        hotel.setName(resultSet.getString("hotel_name"));
        hotel.setStars(resultSet.getByte("stars"));
        hotel.setWebsite(resultSet.getString("website"));
        hotel.setLatitude(resultSet.getBigDecimal("latitude"));
        hotel.setLongitude(resultSet.getBigDecimal("longitude"));
        hotel.setFeatures(HotelFeature.valueOf(resultSet.getString("feature").toUpperCase()));
        tour.setCountry(country);
        tour.setHotel(hotel);
        return tour;
    }
}
