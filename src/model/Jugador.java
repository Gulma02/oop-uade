package model;

public class Jugador {
    private int id;
    private String nombre;
    private String email;
    private int puntajeAcumulado;
    private Caballo caballoSeleccionado;

    public Jugador(int id, String nombre, String email, int puntajeAcumulado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.puntajeAcumulado = puntajeAcumulado;
    }

    public void seleccionarCaballo(Caballo caballo) {
        this.caballoSeleccionado = caballo;
    }

    public void sumarPuntos(int puntos) {
        puntajeAcumulado += puntos;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }

    public Caballo getCaballoSeleccionado() {
        return caballoSeleccionado;
    }
}
