
FROM maven:3.8.7-eclipse-temurin-17 AS builder  
WORKDIR /app
COPY . .
#RUN mvn clean install 
#-DskipTests
RUN mvn clean verify

# Estágio 2: Imagem final
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app


COPY --from=builder /app/target/*.jar app.jar


COPY --from=builder /app/target/surefire-reports /reports/surefire
COPY --from=builder /app/target/failsafe-reports /reports/failsafe
COPY --from=builder /app/target/site /reports/site

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]