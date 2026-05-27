package dto;

public class CaballoDTO {
    private int id;
    private String nombre;
    private double velocidadBase;
    private double resistencia;
    private String tipo;
    private double energiaActual;
    private double distanciaRecorrida;

    public CaballoDTO(int id, String nombre, double velocidadBase, double resistencia, String tipo, double energiaActual, double distanciaRecorrida) {
        this.id = id;
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.resistencia = resistencia;
        this.tipo = tipo;
        this.energiaActual = energiaActual;
        this.distanciaRecorrida = distanciaRecorrida;
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

    public String getTipo() {
        return tipo;
    }

    public double getEnergiaActual() {
        return energiaActual;
    }

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}
