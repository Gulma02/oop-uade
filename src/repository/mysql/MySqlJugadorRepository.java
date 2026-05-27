package repository.mysql;

import model.Jugador;
import persistence.DatabaseConnection;
import repository.JugadorRepository;

import java.sql.*;
import java.sql.Types;

public class MySqlJugadorRepository implements JugadorRepository {
    @Override
    public Jugador guardar(Jugador jugador) {
        String sql = "INSERT INTO jugadores (nombre, email, puntaje_acumulado, caballo_seleccionado_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, jugador.getNombre());
            statement.setString(2, jugador.getEmail());
            statement.setInt(3, jugador.getPuntajeAcumulado());
            // si no tiene caballo seleccionado, guardar null en la base
            if (jugador.getCaballoSeleccionado() != null) {
                statement.setInt(4, jugador.getCaballoSeleccionado().getId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    Jugador guardado = new Jugador(keys.getInt(1), jugador.getNombre(), jugador.getEmail(), jugador.getPuntajeAcumulado());
                    // asignar caballo seleccionado solo si existía
                    if (jugador.getCaballoSeleccionado() != null) {
                        guardado.seleccionarCaballo(jugador.getCaballoSeleccionado());
                    }
                    return guardado;
                }
                throw new SQLException("No se obtuvo el id del jugador");
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo guardar el jugador", e);
        }
    }

    @Override
    public void actualizarPuntaje(Jugador jugador) {
        String sql = "UPDATE jugadores SET puntaje_acumulado = ? WHERE id = ?";

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, jugador.getPuntajeAcumulado());
            statement.setInt(2, jugador.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar el puntaje", e);
        }
    }
}
