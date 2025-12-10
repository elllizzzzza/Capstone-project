package com.db;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class JDBCUtil {

    private final String url;
    private final String username;
    private final String password;

    public JDBCUtil(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void execute(String query, Object... args) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            fill(ps, args);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(String query, Consumer<PreparedStatement> consumer) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            consumer.accept(ps);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Optional<T> findOne(
            String query,
            Function<ResultSet, T> mapper,
            Object... args
    ) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            fill(ps, args);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            T result = mapper.apply(rs);

            if (rs.next()) {
                throw new IllegalStateException("Query returned more than 1 row");
            }

            return Optional.of(result);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args) {
        List<T> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            fill(ps, args);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapper.apply(rs));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fill(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
    }
}
