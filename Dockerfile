#**Stage 1: Build the JAR using Maven**
FROM maven:3.8.4-openjdk-17 AS builder

# Set working directory
WORKDIR /build

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application (without running tests)
RUN mvn clean package -DskipTests

#**Stage 2: Create lightweight runtime image**
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /build/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar","--spring.profiles.active=docker"]
