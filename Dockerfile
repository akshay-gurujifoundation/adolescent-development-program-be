# Use Maven to build the application
FROM maven:3.8.5-openjdk-17 AS build
LABEL maintainer="akshay.pokharkar@gurujifoundation.in"

# Set the working directory in the container
WORKDIR /app

# Copy only the pom.xml and download dependencies (this step will be cached)
COPY pom.xml /app/
RUN mvn dependency:go-offline -B

# Copy the source code into the container
COPY ./ /app/

# Run Maven to clean and package the application
RUN mvn clean package -DskipTests

# Create the final image
FROM openjdk:17
LABEL maintainer="akshay.pokharkar@gurujifoundation.in"

# Copy the JAR file from the build stage
COPY --from=build /app/target/Adolescent-Development-Program-0.0.1.jar /opt/Adolescent-Development-Program-0.0.1.jar

# Expose the application port
EXPOSE 8080

# Define the entry point for the application
ENTRYPOINT ["java", "-jar", "/opt/Adolescent-Development-Program-0.0.1.jar"]
