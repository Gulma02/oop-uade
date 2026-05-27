# Diagrama de clases

```mermaid
classDiagram
    class Main
    class MainView {
        -JuegoController controller
        -Timer timer
        +MainView()
    }
    class JuegoController {
        -JuegoService juegoService
        +listarCaballos() List~CaballoDTO~
        +crearJugador(nombre, email, caballo) JugadorDTO
        +iniciarCarrera() CarreraEstadoDTO
        +avanzarCarrera() CarreraEstadoDTO
    }
    class JuegoService {
        -CaballoRepository caballoRepository
        -JugadorRepository jugadorRepository
        -CarreraRepository carreraRepository
        -Jugador jugadorActual
        -Carrera carreraActual
        +getInstance() JuegoService
        +listarCaballos() List~CaballoDTO~
        +crearJugador(nombre, email, caballoId) JugadorDTO
        +iniciarCarrera(distancia) CarreraEstadoDTO
        +avanzarCarrera() CarreraEstadoDTO
    }
    class PuntajeService {
        +calcularPuntos(posicionJugador) int
    }
    class Jugador {
        -int id
        -String nombre
        -String email
        -int puntajeAcumulado
        -Caballo caballoSeleccionado
        +seleccionarCaballo(caballo)
        +sumarPuntos(puntos)
    }
    class Caballo {
        -int id
        -String nombre
        -double velocidadBase
        -double resistencia
        -double energiaActual
        -double distanciaRecorrida
        -EstrategiaAvance estrategiaAvance
        +avanzar(distanciaTotal, random)
        +reiniciar()
    }
    class Carrera {
        -Jugador jugador
        -Pista pista
        -List~Caballo~ caballos
        -boolean finalizada
        +avanzar()
        +obtenerRanking() List~ResultadoCaballo~
        +obtenerGanador() Caballo
        +obtenerPosicionJugador() int
    }
    class Pista {
        -double distanciaTotal
    }
    class ResultadoCaballo {
        -Caballo caballo
        -int posicion
    }
    class EstrategiaAvance {
        <<interface>>
        +calcularAvance(caballo, distanciaTotal, random) double
        +getTipo() String
    }
    class AvanceVelocista
    class AvanceResistente
    class AvanceEquilibrado
    class CaballoRepository {
        <<interface>>
        +buscarTodos() List~Caballo~
        +buscarPorId(id) Optional~Caballo~
        +guardarInicialesSiNoExisten()
    }
    class JugadorRepository {
        <<interface>>
        +guardar(jugador) Jugador
        +actualizarPuntaje(jugador)
    }
    class CarreraRepository {
        <<interface>>
        +guardarResultado(carrera, jugador, puntosObtenidos)
    }
    class MySqlCaballoRepository
    class MySqlJugadorRepository
    class MySqlCarreraRepository
    class DatabaseConnection {
        -Connection connection
        +getInstance() Connection
    }
    class CaballoDTO
    class JugadorDTO
    class CarreraEstadoDTO

    Main --> MainView
    MainView --> JuegoController
    JuegoController --> JuegoService
    JuegoService --> PuntajeService
    JuegoService --> CaballoRepository
    JuegoService --> JugadorRepository
    JuegoService --> CarreraRepository
    JuegoService --> Carrera
    JuegoService --> CaballoDTO
    JuegoService --> JugadorDTO
    JuegoService --> CarreraEstadoDTO
    Jugador "1" --> "1" Caballo : selecciona
    Carrera "1" --> "1" Jugador
    Carrera "1" --> "1" Pista
    Carrera "1" --> "*" Caballo
    Carrera --> ResultadoCaballo
    Caballo --> EstrategiaAvance
    EstrategiaAvance <|.. AvanceVelocista
    EstrategiaAvance <|.. AvanceResistente
    EstrategiaAvance <|.. AvanceEquilibrado
    CaballoRepository <|.. MySqlCaballoRepository
    JugadorRepository <|.. MySqlJugadorRepository
    CarreraRepository <|.. MySqlCarreraRepository
    MySqlCaballoRepository --> DatabaseConnection
    MySqlJugadorRepository --> DatabaseConnection
    MySqlCarreraRepository --> DatabaseConnection
```

## Conceptos aplicados

- MVC: `MainView` es la vista, `JuegoController` coordina la entrada del usuario y las clases de `model` contienen el negocio.
- DTO: `CaballoDTO`, `JugadorDTO` y `CarreraEstadoDTO` transportan datos entre vista y controlador.
- Singleton: `JuegoService` centraliza el estado de la partida y `DatabaseConnection` centraliza la conexion JDBC.
- DAO: los repositorios separan el acceso a MySQL de las reglas de negocio.
- GRASP Information Expert: `Carrera` determina ranking, ganador y posicion porque conoce pista y caballos.
- SOLID OCP: las variantes de avance se agregan implementando `EstrategiaAvance`, sin modificar `Caballo`.
