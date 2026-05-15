package controllers;

import dto.CaballoDTO;
import dto.CarreraEstadoDTO;
import dto.JugadorDTO;
import services.JuegoService;

import java.util.List;

public class JuegoController {
    private JuegoService juegoService;

    public JuegoController() {
        juegoService = JuegoService.getInstance();
    }

    public List<CaballoDTO> listarCaballos() {
        return juegoService.listarCaballos();
    }

    public JugadorDTO crearJugador(String nombre, String email, CaballoDTO caballo) {
        if (caballo == null) {
            throw new IllegalArgumentException("Debe seleccionar un caballo");
        }
        return juegoService.crearJugador(nombre, email, caballo.getId());
    }

    public CarreraEstadoDTO iniciarCarrera() {
        return juegoService.iniciarCarrera(500);
    }

    public CarreraEstadoDTO avanzarCarrera() {
        return juegoService.avanzarCarrera();
    }
}
