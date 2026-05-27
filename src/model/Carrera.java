package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Carrera {
    private Jugador jugador;
    private Pista pista;
    private List<Caballo> caballos;
    private boolean finalizada;
    private Random random = new Random();

    public Carrera(Jugador jugador, Pista pista, List<Caballo> caballos) {
        this.jugador = jugador;
        this.pista = pista;
        this.caballos = new ArrayList<>(caballos);
        this.caballos.forEach(caballo -> caballo.prepararParaCarrera(random));
    }

    public void avanzar() {
        if (finalizada) {
            return;
        }

        for (Caballo caballo : caballos) {
            caballo.avanzar(pista.getDistanciaTotal(), random);
        }

        finalizada = caballos.stream()
                .anyMatch(caballo -> caballo.getDistanciaRecorrida() >= pista.getDistanciaTotal());
    }

    public List<ResultadoCaballo> obtenerRanking() {
        List<Caballo> ordenados = caballos.stream()
                .sorted(Comparator.comparingDouble(Caballo::getDistanciaRecorrida).reversed())
                .toList();

        List<ResultadoCaballo> resultados = new ArrayList<>();
        for (int i = 0; i < ordenados.size(); i++) {
            resultados.add(new ResultadoCaballo(ordenados.get(i), i + 1));
        }
        return resultados;
    }

    public Caballo obtenerGanador() {
        return obtenerRanking().get(0).getCaballo();
    }

    public int obtenerPosicionJugador() {
        int caballoJugadorId = jugador.getCaballoSeleccionado().getId();
        return obtenerRanking().stream()
                .filter(resultado -> resultado.getCaballo().getId() == caballoJugadorId)
                .findFirst()
                .map(ResultadoCaballo::getPosicion)
                .orElse(caballos.size());
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public Pista getPista() {
        return pista;
    }

    public List<Caballo> getCaballos() {
        return new ArrayList<>(caballos);
    }
}
