package com.epam.travelagency.bean;

import com.epam.travelagency.repository.UserRepository;
import com.epam.travelagency.storage.posgresql.UserDataContext;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {
    @Bean
    public User getUser(){
        return new User();
    }

    @Bean
    public HikariDataSource getDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/travelagencydb");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1623107");
        return dataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    @Bean
    public UserDataContext getUserDataContext(){
        return new UserDataContext(getJdbcTemplate());
    }

    public UserRepository getUserRepository(){
        return new UserRepository();
    }
}
