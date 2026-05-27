package model;

import model.avance.EstrategiaAvance;

import java.util.Random;

public class Caballo {
    private int id;
    private String nombre;
    private double velocidadBase;
    private double resistencia;
    private double energiaActual;
    private double distanciaRecorrida;
    private double factorRendimiento;
    private EstrategiaAvance estrategiaAvance;

    public Caballo(int id, String nombre, double velocidadBase, double resistencia, EstrategiaAvance estrategiaAvance) {
        this.id = id;
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.resistencia = resistencia;
        this.estrategiaAvance = estrategiaAvance;
        reiniciar();
    }

    public void avanzar(double distanciaTotal, Random random) {
        if (distanciaRecorrida >= distanciaTotal) {
            return;
        }

        double avance = estrategiaAvance.calcularAvance(this, distanciaTotal, random);
        distanciaRecorrida = Math.min(distanciaTotal, distanciaRecorrida + avance);
        energiaActual = Math.max(0, energiaActual - calcularDesgaste());
    }

    public void prepararParaCarrera(Random random) {
        energiaActual = 100;
        distanciaRecorrida = 0;
        factorRendimiento = 0.85 + random.nextDouble(0.30);
    }

    public void reiniciar() {
        energiaActual = 100;
        distanciaRecorrida = 0;
        factorRendimiento = 1.0;
    }

    private double calcularDesgaste() {
        return Math.max(0.8, 5.2 - (resistencia / 25.0));
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getVelocidadBase() {
        return velocidadBase;
    }

    public double getResistencia() {
        return resistencia;
    }

    public double getEnergiaActual() {
        return energiaActual;
    }

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public double getFactorRendimiento() {
        return factorRendimiento;
    }

    public String getTipo() {
        return estrategiaAvance.getTipo();
    }
}
