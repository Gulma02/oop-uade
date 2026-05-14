# OOP UADE

Aplicación Java Swing utilizando arquitectura MVC y MySQL con Docker.

---

# Requisitos

Antes de comenzar, asegurarse de tener instalado:

- Java JDK 21 o superior
- Docker Desktop
- Docker Compose

## Verificar instalaciones

```bash
java -version
docker -v
docker compose version
```

---

# Clonar el proyecto

```bash
git clone <URL_DEL_REPOSITORIO>
cd oop-uade
```

---

# Configuración inicial

El proyecto utiliza un archivo `.env` para las variables de entorno.

Ya existe uno incluido en el proyecto, pero verificar que contenga:

```env
MYSQL_DATABASE=oop_uade
MYSQL_USER=oop_user
MYSQL_PASSWORD=oop_password
MYSQL_ROOT_PASSWORD=root_password

DB_HOST=localhost
DB_PORT=3306
DB_NAME=oop_uade
DB_USER=oop_user
DB_PASSWORD=oop_password
```

---

# Levantar el proyecto

## Linux / Mac

Dar permisos de ejecución:

```bash
chmod +x start.sh
```

Luego ejecutar:

```bash
./start.sh
```

---

# ¿Qué hace el script?

El script automáticamente:

- Levanta MySQL con Docker
- Espera a que la base de datos esté disponible
- Compila el proyecto Java
- Ejecuta la aplicación Swing

---

# Detener la base de datos

```bash
docker compose down
```

---

# Estructura del proyecto

```txt
oop-uade/
├── docker/
├── src/
│   ├── controllers/
│   ├── models.dtos/
│   ├── persistence/
│   └── views/
├── docker-compose.yml
├── start.sh
├── .env
└── README.md
```

---

# Solución de problemas

## Error: `docker: command not found`

Docker no está instalado o no está iniciado.

Abrir Docker Desktop e intentar nuevamente.

---

## Error: `java: command not found`

Java no está instalado o no está agregado al PATH.

Instalar JDK 21.

---

## Error: `Port 3306 already in use`

Ya existe una instancia de MySQL corriendo en la máquina.

Cerrar MySQL local o modificar el puerto en `docker-compose.yml`.

---

## Error al conectar a la base de datos

Verificar:

- Que Docker esté corriendo
- Que el contenedor MySQL esté levantado:

```bash
docker ps
```

- Que las variables del `.env` sean correctas

---

# Notas

- La interfaz gráfica Swing corre de forma nativa (NO dentro de Docker)
- Docker se utiliza únicamente para la base de datos MySQL
- No es necesario instalar XQuartz ni herramientas gráficas adicionales

