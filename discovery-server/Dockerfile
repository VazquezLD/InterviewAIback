FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -N install
COPY discovery-server ./discovery-server
WORKDIR /app/discovery-server
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Asegúrate de que la ruta coincida con la carpeta de arriba
COPY --from=build /app/discovery-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]