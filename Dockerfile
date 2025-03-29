FROM openjdk:23-ea-17-slim as build
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:23-ea-17-slim
WORKDIR /app
COPY --from=build /app/boot/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]