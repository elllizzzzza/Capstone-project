package com.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Bean
    public JDBCUtil jdbcUtil(DataSource dataSource) {
        return new JDBCUtil(dataSource);
    }
}

