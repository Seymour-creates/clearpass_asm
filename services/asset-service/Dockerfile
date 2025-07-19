# ---- Build Stage ----
    FROM maven:3.9-eclipse-temurin-21 AS build
    WORKDIR /build
    COPY . .
    RUN mvn -pl services/asset-service -am -B -DskipTests package
    
    # ---- Runtime Stage ----
    FROM eclipse-temurin:21-jre-alpine
    WORKDIR /app
    COPY --from=build /build/services/asset-service/target/asset-service-0.0.1-SNAPSHOT.jar app.jar
    ENTRYPOINT ["java", "-jar", "app.jar"]