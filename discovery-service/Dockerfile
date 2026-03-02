FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn install -N -DskipTests
COPY discovery-service/pom.xml discovery-service/
COPY discovery-service/src discovery-service/src
RUN mvn clean package -DskipTests -f discovery-service/pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/discovery-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]