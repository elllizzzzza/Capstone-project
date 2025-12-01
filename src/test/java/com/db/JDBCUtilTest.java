package com.db;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCUtilTest {

    static JDBCUtil db;

    @BeforeAll
    static void setup() {
        db = new JDBCUtil(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "sa",
                ""
        );

        db.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100))");
        db.execute("INSERT INTO users VALUES (?,?)", 1, "Anna");
        db.execute("INSERT INTO users VALUES (?,?)", 2, "Eliza");
        db.execute("INSERT INTO users VALUES (?,?)", 3, "Meri");
    }

    @Test
    void execute_WithArgs_ShouldInsertNewRow() {
        db.execute("INSERT INTO users VALUES (?,?)", 4, "John");

        String name = db.findOne(
                "SELECT name FROM users WHERE id=?",
                rs -> {
                    try {
                        return rs.getString("name");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                4
        );

        assertEquals("John", name);
    }

    @Test
    void execute_WithConsumer_ShouldInsertNewRow() {
        db.execute("INSERT INTO users VALUES (?,?)", ps -> {
            try {
                ps.setInt(1, 5);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                ps.setString(2, "Bob");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        String name = db.findOne(
                "SELECT name FROM users WHERE id=?",
                rs -> {
                    try {
                        return rs.getString("name");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                5
        );

        assertEquals("Bob", name);
    }

    @Test
    void findOne_ShouldReturnSingleRecord() {
        String name = db.findOne(
                "SELECT name FROM users WHERE id=?",
                rs -> {
                    try {
                        return rs.getString("name");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                1
        );

        assertEquals("Anna", name);
    }

    @Test
    void findOne_WhenNoResults_ShouldReturnNull() {
        String name = db.findOne(
                "SELECT name FROM users WHERE id=?",
                rs -> {
                    try {
                        return rs.getString("name");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                99
        );

        assertNull(name);
    }

    @Test
    void findOne_WhenMultipleResults_ShouldThrowException() {
        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> db.findOne(
                        "SELECT name FROM users WHERE name LIKE '%a%'",
                        rs -> {
                            try {
                                return rs.getString("name");
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
        );

        assertTrue(ex.getMessage().contains("Query returned more than 1 row"));
    }

    @Test
    void findMany_ShouldReturnAllRows() {
        List<String> names = db.findMany(
                "SELECT name FROM users ORDER BY id",
                rs -> {
                    try {
                        return rs.getString("name");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        assertEquals(3, names.size());
        assertEquals("Anna", names.get(0));
        assertEquals("Eliza", names.get(1));
    }

    @Test
    void findMany_ShouldReturnEmptyList_WhenNoResults() {
        List<String> names = db.findMany(
                "SELECT name FROM users WHERE id > 100",
                rs -> {
                    try {
                        return rs.getString("name");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        assertTrue(names.isEmpty());
    }
}
