package dto;

import java.util.List;

public class CarreraEstadoDTO {
    private List<CaballoDTO> caballos;
    private double distanciaTotal;
    private boolean finalizada;
    private String ganador;
    private int posicionJugador;
    private int puntosObtenidos;
    private int puntajeAcumulado;

    public CarreraEstadoDTO(List<CaballoDTO> caballos, double distanciaTotal, boolean finalizada, String ganador, int posicionJugador, int puntosObtenidos, int puntajeAcumulado) {
        this.caballos = caballos;
        this.distanciaTotal = distanciaTotal;
        this.finalizada = finalizada;
        this.ganador = ganador;
        this.posicionJugador = posicionJugador;
        this.puntosObtenidos = puntosObtenidos;
        this.puntajeAcumulado = puntajeAcumulado;
    }

    public List<CaballoDTO> getCaballos() {
        return caballos;
    }

    public double getDistanciaTotal() {
        return distanciaTotal;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public String getGanador() {
        return ganador;
    }

    public int getPosicionJugador() {
        return posicionJugador;
    }

    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }
}
