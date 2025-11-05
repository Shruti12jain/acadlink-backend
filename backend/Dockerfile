# Use official OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8081

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","target/backend-0.0.1-SNAPSHOT.jar"]
