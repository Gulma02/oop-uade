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
        validarTexto(nombre, "El nombre es obligatorio");
        validarTexto(email, "El mail es obligatorio");

        Caballo caballo = caballoRepository.buscarPorId(caballoId)
                .orElseThrow(() -> new IllegalArgumentException("El caballo seleccionado no existe"));
        Jugador jugador = new Jugador(0, nombre.trim(), email.trim(), 0);
        jugador.seleccionarCaballo(caballo);
        jugadorActual = jugadorRepository.guardar(jugador);
        return toDTO(jugadorActual);
    }

    public CarreraEstadoDTO iniciarCarrera(double distancia) {
        if (jugadorActual == null) {
            throw new IllegalStateException("Primero debe crear un jugador");
        }

        List<Caballo> caballos = caballoRepository.buscarTodos();
        jugadorActual.seleccionarCaballo(caballos.stream()
                .filter(caballo -> caballo.getId() == jugadorActual.getCaballoSeleccionado().getId())
                .findFirst()
                .orElse(jugadorActual.getCaballoSeleccionado()));

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
