package com.epam.esm.gcs.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig("/db/dev.properties");
        //todo: move driverClassName to properties
        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config);
    }
}
