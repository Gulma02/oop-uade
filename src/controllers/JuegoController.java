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

    /**
     * Crea un jugador sin seleccionar un caballo.  El caballo podrá elegirse más tarde al iniciar la carrera.
     *
     * @param nombre nombre del jugador
     * @param email  email del jugador
     * @return el jugador creado
     */
    public JugadorDTO crearJugador(String nombre, String email) {
        return juegoService.crearJugador(nombre, email);
    }

    /**
     * Crea un jugador y selecciona un caballo en una sola operación.  Se mantiene por compatibilidad.
     *
     * @param nombre nombre del jugador
     * @param email email del jugador
     * @param caballo caballo seleccionado
     * @return el jugador creado
     * @deprecated Use {@link #crearJugador(String, String)} para crear el jugador y seleccione el caballo al iniciar la carrera.
     */
    @Deprecated
    public JugadorDTO crearJugador(String nombre, String email, CaballoDTO caballo) {
        if (caballo == null) {
            throw new IllegalArgumentException("Debe seleccionar un caballo");
        }
        return juegoService.crearJugador(nombre, email, caballo.getId());
    }

    /**
     * Inicia una carrera utilizando el caballo indicado.  Se le asigna al jugador actual el caballo
     * seleccionado y se crea una nueva carrera con los caballos disponibles.
     *
     * @param caballo el caballo que conducirá el jugador en la carrera
     * @return el estado inicial de la carrera
     */
    public CarreraEstadoDTO iniciarCarrera(CaballoDTO caballo) {
        return juegoService.iniciarCarrera(500, caballo.getId());
    }

    /**
     * Inicia una carrera con la distancia predeterminada utilizando el caballo que ya tenga seleccionado
     * el jugador actual.  Se mantiene por compatibilidad con el comportamiento antiguo.
     *
     * @return el estado inicial de la carrera
     * @deprecated Utilice {@link #iniciarCarrera(CaballoDTO)} para seleccionar el caballo al iniciar la carrera.
     */
    @Deprecated
    public CarreraEstadoDTO iniciarCarrera() {
        return juegoService.iniciarCarrera(500);
    }

    public CarreraEstadoDTO avanzarCarrera() {
        return juegoService.avanzarCarrera();
    }
}
