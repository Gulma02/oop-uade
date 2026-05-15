package model.avance;

import model.Caballo;

import java.util.Random;

public class AvanceResistente implements EstrategiaAvance {
    @Override
    public double calcularAvance(Caballo caballo, double distanciaTotal, Random random) {
        double progreso = caballo.getDistanciaRecorrida() / distanciaTotal;
        double bonusFinal = progreso > 0.60 ? 1.25 : 1.0;
        double energia = Math.max(0.65, caballo.getEnergiaActual() / 100.0);
        return caballo.getVelocidadBase() * bonusFinal * energia + random.nextDouble(1.8);
    }

    @Override
    public String getTipo() {
        return "RESISTENTE";
    }
}
