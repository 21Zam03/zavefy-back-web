# Usar una imagen base de Java
FROM eclipse-temurin:17-jdk

# Fija la zona horaria del contenedor (las imágenes base vienen en UTC por defecto)
ENV TZ=America/Lima

# Crear directorio de la app
WORKDIR /app

# Copiar el jar generado por Maven/Gradle
COPY target/ventas-bodega-0.0.1.jar app.jar

# Exponer el puerto del Eureka Server
EXPOSE 8080