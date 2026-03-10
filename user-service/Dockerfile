FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn install -N -DskipTests
COPY shared-security/pom.xml shared-security/
COPY shared-security/src shared-security/src
RUN mvn install -DskipTests -f shared-security/pom.xml
COPY user-service/pom.xml user-service/
COPY user-service/src user-service/src
RUN mvn clean package -DskipTests -f user-service/pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]