
# Use a base image with JDK (Java Development Kit)
#FROM openjdk:17-jdk-slim

# Set the working directory
#WORKDIR /app

# Copy the JAR file into the container (source and destination are specified)
#COPY build/libs/overtime-services-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
#EXPOSE 8092

# Command to run the application
#ENTRYPOINT ["java","-jar","app.jar"]

##########################33333

# Build stage
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy gradle wrapper and make it executable
COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

# Copy build files
COPY build.gradle settings.gradle ./

# Copy source code
COPY src src

# Build the application, skipping tests for faster build
RUN ./gradlew build -x test

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar ./

# Expose port 8080
EXPOSE 8094

# Run the application
CMD ["sh", "-c", "java -jar overtime-services-0.0.1-SNAPSHOT.jar"]
