package model.avance;

import model.Caballo;

import java.util.Random;

public class AvanceEquilibrado implements EstrategiaAvance {
    @Override
    public double calcularAvance(Caballo caballo, double distanciaTotal, Random random) {
        double energia = Math.max(0.58, caballo.getEnergiaActual() / 100.0);
        return caballo.getVelocidadBase() * energia + random.nextDouble(2.0);
    }

    @Override
    public String getTipo() {
        return "EQUILIBRADO";
    }
}
