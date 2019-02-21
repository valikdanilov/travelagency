package com.epam.travelagency.repository;

import com.epam.travelagency.bean.Hotel;
import com.epam.travelagency.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository
public class HotelRepository extends Repository<Hotel> {
    @Override
    public void create(Hotel entity) {
        storage.create(entity);
    }

    @Override
    public Hotel read(Integer id) {
        return storage.read(id);
    }

    @Override
    public void update(Hotel entity) {
        storage.update(entity);
    }

    @Override
    public void delete(Integer id) {
        storage.delete(id);
    }

    public List<Hotel> read(){
        return storage.read();
    }
}
