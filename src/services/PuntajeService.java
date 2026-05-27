package services;

public class PuntajeService {
    public int calcularPuntos(int posicionJugador) {
        if (posicionJugador == 1) {
            return 100;
        }
        if (posicionJugador == 2) {
            return 50;
        }
        return 10;
    }
}
