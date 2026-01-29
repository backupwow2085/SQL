package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {

    private static final QueryRunner runner = new QueryRunner();
    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/app");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "app");
    private static final String DB_PASS = System.getenv().getOrDefault("DB_PASS", "pass");

    private SQLHelper() {
    }

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConnection()) {
            runner.update(conn, "DELETE FROM card_transactions;");
            runner.update(conn, "DELETE FROM auth_codes;");
            runner.update(conn, "DELETE FROM cards;");
            runner.update(conn, "DELETE FROM users;");
        }
    }

    @SneakyThrows
    public static String getAuthCode(String login) {
        var sql =
                "SELECT ac.code " +
                        "FROM auth_codes ac " +
                        "JOIN users u ON ac.user_id = u.id " +
                        "WHERE u.login = ? " +
                        "ORDER BY ac.created DESC " +
                        "LIMIT 1;";

        try (var conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        }
    }
}
