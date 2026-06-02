# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar el caché de capas de Docker.
# Esto evita tener que volver a descargar las dependencias si solo cambiaste código en el 'src'.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos el proyecto generando el archivo .jar (saltamos los tests para un build más rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Producción (Runtime)
# Usamos una imagen mucho más ligera que solo contiene el entorno de ejecución de Java (JRE)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el archivo .jar compilado desde la etapa anterior ("build")
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto por defecto (Render igual se encargará de inyectar el suyo propio)
EXPOSE 8080

# Comando final que se ejecuta al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
