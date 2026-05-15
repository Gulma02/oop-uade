package model.avance;

import model.Caballo;

import java.util.Random;

public class AvanceVelocista implements EstrategiaAvance {
    @Override
    public double calcularAvance(Caballo caballo, double distanciaTotal, Random random) {
        double progreso = caballo.getDistanciaRecorrida() / distanciaTotal;
        double bonusInicio = progreso < 0.35 ? 1.35 : 0.85;
        double energia = Math.max(0.50, caballo.getEnergiaActual() / 100.0);
        return caballo.getVelocidadBase() * bonusInicio * energia + random.nextDouble(2.5);
    }

    @Override
    public String getTipo() {
        return "VELOCISTA";
    }
}
