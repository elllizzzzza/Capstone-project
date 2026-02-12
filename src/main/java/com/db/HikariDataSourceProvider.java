//package com.db;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//import javax.sql.DataSource;
//
//public class HikariDataSourceProvider {
//
//    public static DataSource createDataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:postgresql://localhost:5432/DBhospital");
//        config.setUsername("postgres");
//        config.setPassword("1234");
//
//        config.setMaximumPoolSize(10);
//        config.setMinimumIdle(2);
//        config.setIdleTimeout(30000);
//        config.setConnectionTimeout(10000);
//        config.setMaxLifetime(60000);
//
//        return new HikariDataSource(config);
//    }
//}
