# Decisiones de diseno

El proyecto implementa la consigna Carrera de Caballos con una aplicacion de escritorio Java Swing, arquitectura MVC y persistencia MySQL por JDBC.

## Capas

- `model`: entidades y reglas del dominio. Incluye `Jugador`, `Caballo`, `Carrera`, `Pista` y estrategias de avance.
- `views`: interfaz Swing. No accede directo al modelo ni a la base de datos.
- `controllers`: recibe acciones de la vista y las delega al servicio.
- `dto`: objetos simples para transportar datos entre vista y controlador.
- `services`: coordina casos de uso y reglas de negocio.
- `repository`: interfaces DAO.
- `repository.mysql`: implementaciones JDBC para MySQL.
- `persistence`: `DatabaseConnection` como Singleton de conexion.

## Reglas principales

- La carrera finaliza cuando un caballo alcanza o supera la distancia total de la pista.
- El puntaje del jugador depende de su posicion: primero 100, segundo 50, participa 10.
- Cada caballo usa una estrategia de avance distinta: velocista, resistente o equilibrado.
- La energia baja durante la carrera y afecta el avance posterior.

## Relacion con GRASP y SOLID

- Controller: `JuegoController` gestiona eventos del sistema desde la UI.
- Information Expert: `Carrera` calcula ganador y posicion porque conoce caballos y pista.
- Low Coupling: la UI depende del controlador y de DTOs, no de entidades ni SQL.
- High Cohesion: cada clase tiene una responsabilidad acotada.
- SRP: el calculo de puntaje esta separado en `PuntajeService`; la persistencia esta en DAOs.
- OCP: se pueden agregar tipos de caballo creando nuevas implementaciones de `EstrategiaAvance`.
- DIP: `JuegoService` usa interfaces de repositorio, no clases JDBC como contrato principal.
