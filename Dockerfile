# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory to /sourEarth
WORKDIR /UrlShorneter

# Copy the executable jar file and the application.properties file to the container
COPY /target/UrlShortener-0.0.1-SNAPSHOT.jar /UrlShorneter/

# Set the command to run the Spring Boot application
CMD ["java", "-jar", "UrlShortener-0.0.1-SNAPSHOT.jar"]