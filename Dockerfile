FROM maven:3.8.4-openjdk-17-slim

COPY . .
RUN mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/salve-food-api-0.0.1-SNAPSHOT.jar"]
