FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

CMD sh -c "javac -d out src/**/*.java src/*.java && java -cp out Main"