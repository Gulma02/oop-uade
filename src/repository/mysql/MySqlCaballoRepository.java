package repository.mysql;

import model.Caballo;
import model.avance.EstrategiaAvanceFactory;
import persistence.DatabaseConnection;
import repository.CaballoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlCaballoRepository implements CaballoRepository {
    @Override
    public List<Caballo> buscarTodos() {
        String sql = "SELECT id, nombre, velocidad_base, resistencia, tipo FROM caballos ORDER BY id";
        List<Caballo> caballos = new ArrayList<>();

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                caballos.add(mapearCaballo(resultSet));
            }
            return caballos;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudieron consultar los caballos", e);
        }
    }

    @Override
    public Optional<Caballo> buscarPorId(int id) {
        String sql = "SELECT id, nombre, velocidad_base, resistencia, tipo FROM caballos WHERE id = ?";

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearCaballo(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo buscar el caballo", e);
        }
    }

    @Override
    public void guardarInicialesSiNoExisten() {
        if (!buscarTodos().isEmpty()) {
            return;
        }

        String sql = "INSERT INTO caballos (nombre, velocidad_base, resistencia, tipo) VALUES (?, ?, ?, ?)";
        Object[][] datos = {
                {"Relampago", 16.0, 58.0, "VELOCISTA"},
                {"Trueno", 13.5, 86.0, "RESISTENTE"},
                {"Flecha", 14.8, 72.0, "EQUILIBRADO"},
                {"Tormenta", 15.2, 65.0, "VELOCISTA"},
                {"Noble", 13.9, 90.0, "RESISTENTE"}
        };

        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            for (Object[] fila : datos) {
                statement.setString(1, (String) fila[0]);
                statement.setDouble(2, (double) fila[1]);
                statement.setDouble(3, (double) fila[2]);
                statement.setString(4, (String) fila[3]);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudieron crear los caballos iniciales", e);
        }
    }

    private Caballo mapearCaballo(ResultSet resultSet) throws SQLException {
        String tipo = resultSet.getString("tipo");
        return new Caballo(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getDouble("velocidad_base"),
                resultSet.getDouble("resistencia"),
                EstrategiaAvanceFactory.crear(tipo)
        );
    }
}
