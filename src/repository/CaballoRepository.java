package repository;

import model.Caballo;

import java.util.List;
import java.util.Optional;

public interface CaballoRepository {
    List<Caballo> buscarTodos();

    Optional<Caballo> buscarPorId(int id);

    void guardarInicialesSiNoExisten();
}
