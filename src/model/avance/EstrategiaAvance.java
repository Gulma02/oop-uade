package model.avance;

import model.Caballo;

import java.util.Random;

public interface EstrategiaAvance {
    double calcularAvance(Caballo caballo, double distanciaTotal, Random random);

    String getTipo();
}
