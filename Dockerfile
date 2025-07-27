FROM eclipse-temurin:18-jdk-alpine AS runner

WORKDIR /app
COPY build/libs/market-order-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
