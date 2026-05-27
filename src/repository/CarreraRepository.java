package repository;

import model.Carrera;
import model.Jugador;

public interface CarreraRepository {
    void guardarResultado(Carrera carrera, Jugador jugador, int puntosObtenidos);
}
