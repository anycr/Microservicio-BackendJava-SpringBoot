# Usa una imagen base oficial de OpenJDK 21
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR (o WAR) construido a la imagen
# El nombre del JAR/WAR depende de tu configuración en pom.xml/build.gradle
COPY target/task-service-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que se ejecutará tu aplicación (8080 es el predeterminado de Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]