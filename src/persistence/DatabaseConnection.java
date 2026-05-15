package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
                crearTablas();
            } catch (SQLException e) {
                throw new RuntimeException("Error connecting to DB", e);
            }
        }

        return connection;
    }

    private static void crearTablas() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS caballos (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(80) NOT NULL,
                        velocidad_base DOUBLE NOT NULL,
                        resistencia DOUBLE NOT NULL,
                        tipo VARCHAR(30) NOT NULL
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS jugadores (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(80) NOT NULL,
                        email VARCHAR(120) NOT NULL,
                        puntaje_acumulado INT NOT NULL DEFAULT 0,
                        caballo_seleccionado_id INT NULL,
                        FOREIGN KEY (caballo_seleccionado_id) REFERENCES caballos(id)
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS carreras (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        jugador_id INT NOT NULL,
                        caballo_jugador_id INT NOT NULL,
                        ganador VARCHAR(80) NOT NULL,
                        posicion_jugador INT NOT NULL,
                        puntos_obtenidos INT NOT NULL,
                        fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (jugador_id) REFERENCES jugadores(id),
                        FOREIGN KEY (caballo_jugador_id) REFERENCES caballos(id)
                    )
                    """);
        }
    }
}
