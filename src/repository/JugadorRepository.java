package repository;

import model.Jugador;

public interface JugadorRepository {
    Jugador guardar(Jugador jugador);

    void actualizarPuntaje(Jugador jugador);
}
