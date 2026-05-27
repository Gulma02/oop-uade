package model;

public class ResultadoCaballo {
    private Caballo caballo;
    private int posicion;

    public ResultadoCaballo(Caballo caballo, int posicion) {
        this.caballo = caballo;
        this.posicion = posicion;
    }

    public Caballo getCaballo() {
        return caballo;
    }

    public int getPosicion() {
        return posicion;
    }
}
