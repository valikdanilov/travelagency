package com.epam.travelagency.repository;

import com.epam.travelagency.bean.AbstractEntity;
import com.epam.travelagency.bean.User;
import com.epam.travelagency.storage.DataContext;
import com.epam.travelagency.storage.posgresql.UserDataContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class Repository<T extends AbstractEntity> {

    protected DataContext<T> storage;

    @Autowired
    public void setDataContext(DataContext<T> storage) {
        this.storage = storage;
    }

    public abstract void create(T entity);

    public abstract T read(Integer id);

    public abstract void update(T entity);

    public abstract void delete(Integer id);
}