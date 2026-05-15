package repository.mysql;

import model.Carrera;
import model.Jugador;
import persistence.DatabaseConnection;
import repository.CarreraRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlCarreraRepository implements CarreraRepository {
    @Override
    public void guardarResultado(Carrera carrera, Jugador jugador, int puntosObtenidos) {
        String sql = """
                INSERT INTO carreras (jugador_id, caballo_jugador_id, ganador, posicion_jugador, puntos_obtenidos)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, jugador.getId());
            statement.setInt(2, jugador.getCaballoSeleccionado().getId());
            statement.setString(3, carrera.obtenerGanador().getNombre());
            statement.setInt(4, carrera.obtenerPosicionJugador());
            statement.setInt(5, puntosObtenidos);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo guardar la carrera", e);
        }
    }
}
