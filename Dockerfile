# Start with OpenJDK 21
FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Copy everything from current directory to container
COPY . .

# Clean and package the application
RUN ./mvnw clean package

# Expose port 8080 for external access
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/crocobet-0.0.1-SNAPSHOT.jar"]
