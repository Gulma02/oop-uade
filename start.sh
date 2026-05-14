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

javac -d out $(find src -name "*.java")

echo "🖥️ Iniciando aplicación Swing..."

java -cp out Main