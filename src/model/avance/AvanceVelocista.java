package model.avance;

import model.Caballo;

import java.util.Random;

public class AvanceVelocista implements EstrategiaAvance {
    @Override
    public double calcularAvance(Caballo caballo, double distanciaTotal, Random random) {
        double progreso = caballo.getDistanciaRecorrida() / distanciaTotal;
        double bonusInicio = progreso < 0.25 ? 1.08 : 0.90;
        double energia = 0.65 + (caballo.getEnergiaActual() / 100.0) * 0.35;
        double variacion = 0.75 + random.nextDouble(0.50);
        return caballo.getVelocidadBase() * bonusInicio * energia * variacion * caballo.getFactorRendimiento()
                + random.nextDouble(2.5);
    }

    @Override
    public String getTipo() {
        return "VELOCISTA";
    }
}
