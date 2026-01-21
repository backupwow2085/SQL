package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {

    private static final QueryRunner runner = new QueryRunner();
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/app");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "app");
    private static final String DB_PASS = System.getenv().getOrDefault("DB_PASS", "pass");

    private SQLHelper() {
    }

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Cleans DB tables to avoid duplicate demo data between runs.
     */
    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConnection()) {
            runner.update(conn, "DELETE FROM card_transactions;");
            runner.update(conn, "DELETE FROM auth_codes;");
            runner.update(conn, "DELETE FROM cards;");
            runner.update(conn, "DELETE FROM users;");
        }
    }

    /**
     * Inserts a user with a BCrypt-hashed password.
     */
    @SneakyThrows
    public static void createUser(String login, String rawPassword) {
        var hashed = encoder.encode(rawPassword);
        try (var conn = getConnection()) {
            runner.update(
                    conn,
                    "INSERT INTO users (id, login, password, status) VALUES (UUID(), ?, ?, 'active');",
                    login,
                    hashed
            );
        }
    }

    /**
     * Resets user status to active before tests.
     */
    @SneakyThrows
    public static void resetUserStatus(String login) {
        try (var conn = getConnection()) {
            runner.update(conn, "UPDATE users SET status = 'active' WHERE login = ?;", login);
        }
    }

    /**
     * Gets the latest verification code for a user.
     */
    @SneakyThrows
    public static String getAuthCode(String login) {
        var sql =
                "SELECT ac.code " +
                        "FROM auth_codes ac " +
                        "JOIN users u ON ac.user_id = u.id " +
                        "WHERE u.login = ? " +
                        "ORDER BY ac.created DESC " +
                        "LIMIT 1;";

        var deadlineMs = System.currentTimeMillis() + 5000;
        String code = null;
        while (code == null && System.currentTimeMillis() < deadlineMs) {
            try (var conn = getConnection()) {
                code = runner.query(conn, sql, new ScalarHandler<>(), login);
            }
            if (code == null) {
                Thread.sleep(300);
            }
        }
        return code;
    }
}
