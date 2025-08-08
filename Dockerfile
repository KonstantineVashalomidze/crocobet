# Start with OpenJDK 21
FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Make mvnw executable and download dependencies
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

RUN ./mvnw package -DskipTests -B

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/crocobet-0.0.1-SNAPSHOT.jar"]