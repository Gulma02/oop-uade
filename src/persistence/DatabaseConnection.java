package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getInstance() {
        if (connection == null) {
            try {

                String host = System.getenv().getOrDefault("DB_HOST", "localhost");
                String port = System.getenv().getOrDefault("DB_PORT", "3306");
                String db = System.getenv().getOrDefault("DB_NAME", "oop_uade");
                String user = System.getenv().getOrDefault("DB_USER", "oop_user");
                String password = System.getenv().getOrDefault("DB_PASSWORD", "oop_password");

                String url = "jdbc:mysql://" + host + ":" + port + "/" + db;

                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new RuntimeException("Error connecting to DB", e);
            }
        }

        return connection;
    }
}