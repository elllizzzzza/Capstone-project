package com.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ThreadedSimulation {

    public static void main(String[] args) throws SQLException, InterruptedException {
//        var ds = new CustomDataSource(
//                java.sql.DriverManager.getConnection(
//                        "jdbc:postgresql://localhost:5432/educational_db", "postgres", "1234")
//        );
        DataSource ds = HikariDataSourceProvider.createDataSource();


        int threads = 5;
        Thread[] workers = new Thread[threads];

        long start = System.currentTimeMillis();

        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                try (Connection conn = ds.getConnection();
                     PreparedStatement ps = conn.prepareStatement("SELECT pg_sleep(3)")) {
                    ps.execute();
                    System.out.println(Thread.currentThread().getName() + " finished.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            workers[i].start();
        }

        for (Thread t : workers) {
            t.join();
        }

        long end = System.currentTimeMillis();
        System.out.println("Total time: " + (end - start) / 1000.0 + " seconds");
    }
}
