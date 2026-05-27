package services;

import dto.CaballoDTO;
import dto.CarreraEstadoDTO;
import dto.JugadorDTO;
import model.Caballo;
import model.Carrera;
import model.Jugador;
import model.Pista;
import repository.CaballoRepository;
import repository.CarreraRepository;
import repository.JugadorRepository;
import repository.mysql.MySqlCaballoRepository;
import repository.mysql.MySqlCarreraRepository;
import repository.mysql.MySqlJugadorRepository;

import java.util.List;

public class JuegoService {
    private static JuegoService instance;

    private CaballoRepository caballoRepository;
    private JugadorRepository jugadorRepository;
    private CarreraRepository carreraRepository;
    private PuntajeService puntajeService;
    private Jugador jugadorActual;
    private Carrera carreraActual;
    private boolean resultadoGuardado;

    private JuegoService() {
        caballoRepository = new MySqlCaballoRepository();
        jugadorRepository = new MySqlJugadorRepository();
        carreraRepository = new MySqlCarreraRepository();
        puntajeService = new PuntajeService();
        caballoRepository.guardarInicialesSiNoExisten();
    }

    public static JuegoService getInstance() {
        if (instance == null) {
            instance = new JuegoService();
        }
        return instance;
    }

    public List<CaballoDTO> listarCaballos() {
        return caballoRepository.buscarTodos().stream()
                .map(this::toDTO)
                .toList();
    }

    public JugadorDTO crearJugador(String nombre, String email, int caballoId) {
        // Deprecated: mantiene compatibilidad pero delega en la nueva creación y selección de caballo al iniciar la carrera.
        validarTexto(nombre, "El nombre es obligatorio");
        validarTexto(email, "El mail es obligatorio");
        Caballo caballo = caballoRepository.buscarPorId(caballoId)
                .orElseThrow(() -> new IllegalArgumentException("El caballo seleccionado no existe"));
        Jugador jugador = new Jugador(0, nombre.trim(), email.trim(), 0);
        jugador.seleccionarCaballo(caballo);
        jugadorActual = jugadorRepository.guardar(jugador);
        return toDTO(jugadorActual);
    }

    /**
     * Crea un jugador sin caballo seleccionado.  El caballo se elegirá al iniciar la carrera.
     *
     * @param nombre nombre del jugador
     * @param email  email del jugador
     * @return el jugador creado
     */
    public JugadorDTO crearJugador(String nombre, String email) {
        validarTexto(nombre, "El nombre es obligatorio");
        validarTexto(email, "El mail es obligatorio");
        Jugador jugador = new Jugador(0, nombre.trim(), email.trim(), 0);
        // no seleccionar caballo por ahora
        jugadorActual = jugadorRepository.guardar(jugador);
        return toDTO(jugadorActual);
    }

    public CarreraEstadoDTO iniciarCarrera(double distancia) {
        // Deprecated: asume que el jugador ya tiene un caballo seleccionado.
        if (jugadorActual == null) {
            throw new IllegalStateException("Primero debe crear un jugador");
        }
        if (jugadorActual.getCaballoSeleccionado() == null) {
            throw new IllegalStateException("Debe seleccionar un caballo antes de iniciar la carrera");
        }
        List<Caballo> caballos = caballoRepository.buscarTodos();
        // Actualizar referencia del caballo seleccionado al listado actual
        jugadorActual.seleccionarCaballo(caballos.stream()
                .filter(caballo -> caballo.getId() == jugadorActual.getCaballoSeleccionado().getId())
                .findFirst()
                .orElse(jugadorActual.getCaballoSeleccionado()));

        carreraActual = new Carrera(jugadorActual, new Pista(distancia), caballos);
        resultadoGuardado = false;
        return crearEstado(0);
    }

    /**
     * Inicia una carrera asignando al jugador actual el caballo indicado y preparando una pista con la distancia dada.
     *
     * @param distancia distancia total de la pista
     * @param caballoId id del caballo seleccionado
     * @return el estado inicial de la carrera
     */
    public CarreraEstadoDTO iniciarCarrera(double distancia, int caballoId) {
        if (jugadorActual == null) {
            throw new IllegalStateException("Primero debe crear un jugador");
        }
        Caballo caballo = caballoRepository.buscarPorId(caballoId)
                .orElseThrow(() -> new IllegalArgumentException("El caballo seleccionado no existe"));
        // seleccionar caballo para el jugador antes de iniciar
        jugadorActual.seleccionarCaballo(caballo);
        List<Caballo> caballos = caballoRepository.buscarTodos();
        carreraActual = new Carrera(jugadorActual, new Pista(distancia), caballos);
        resultadoGuardado = false;
        return crearEstado(0);
    }

    public CarreraEstadoDTO avanzarCarrera() {
        if (carreraActual == null) {
            throw new IllegalStateException("No hay una carrera iniciada");
        }

        carreraActual.avanzar();
        int puntos = 0;
        if (carreraActual.isFinalizada() && !resultadoGuardado) {
            puntos = puntajeService.calcularPuntos(carreraActual.obtenerPosicionJugador());
            jugadorActual.sumarPuntos(puntos);
            jugadorRepository.actualizarPuntaje(jugadorActual);
            carreraRepository.guardarResultado(carreraActual, jugadorActual, puntos);
            resultadoGuardado = true;
        }
        return crearEstado(puntos);
    }

    private CarreraEstadoDTO crearEstado(int puntosObtenidos) {
        String ganador = carreraActual.isFinalizada() ? carreraActual.obtenerGanador().getNombre() : "";
        int posicion = carreraActual.isFinalizada() ? carreraActual.obtenerPosicionJugador() : 0;

        return new CarreraEstadoDTO(
                carreraActual.getCaballos().stream().map(this::toDTO).toList(),
                carreraActual.getPista().getDistanciaTotal(),
                carreraActual.isFinalizada(),
                ganador,
                posicion,
                puntosObtenidos,
                jugadorActual.getPuntajeAcumulado()
        );
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private CaballoDTO toDTO(Caballo caballo) {
        return new CaballoDTO(
                caballo.getId(),
                caballo.getNombre(),
                caballo.getVelocidadBase(),
                caballo.getResistencia(),
                caballo.getTipo(),
                caballo.getEnergiaActual(),
                caballo.getDistanciaRecorrida()
        );
    }

    private JugadorDTO toDTO(Jugador jugador) {
        return new JugadorDTO(jugador.getId(), jugador.getNombre(), jugador.getEmail(), jugador.getPuntajeAcumulado());
    }
}
