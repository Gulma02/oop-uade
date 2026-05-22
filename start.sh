#!/bin/bash

echo "🚀 Levantando MySQL..."
docker compose up -d

echo "⏳ Esperando MySQL..."

until docker exec oop-uade-mysql mysqladmin ping -h"localhost" --silent; do
  sleep 2
done

echo "✅ MySQL listo"

echo "☕ Compilando Java..."

mkdir -p out
mkdir -p lib

CONNECTOR="lib/mysql-connector-j-8.4.0.jar"

if [ ! -f "$CONNECTOR" ]; then
  echo "📦 Descargando MySQL Connector/J..."
  curl -L -o "$CONNECTOR" "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar"
fi

javac -cp "$CONNECTOR" -d out $(find src -name "*.java")

echo "🖥️ Iniciando aplicación Swing..."

java -cp "out:$CONNECTOR" Main
