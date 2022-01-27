package com.epam.esm.gcs.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/db/application-${profile.active:test}.properties")
public class DataSourceConfig {

    private Environment environment;

    @Autowired
    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty("dataSource.jdbcUrl"));
        config.setDriverClassName(environment.getProperty("dataSource.driverClassName"));
        config.setUsername(environment.getProperty("dataSource.user"));
        config.setPassword(environment.getProperty("dataSource.password"));
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
