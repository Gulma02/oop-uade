package dto;

public class JugadorDTO {
    private int id;
    private String nombre;
    private String email;
    private int puntajeAcumulado;

    public JugadorDTO(int id, String nombre, String email, int puntajeAcumulado) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.puntajeAcumulado = puntajeAcumulado;
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
}
