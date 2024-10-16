# Adolescent Development Program Backend

This project is a Spring Boot application designed to manage the backend services for the Adolescent Development Program. The project uses Java 17 and is configured to run on IntelliJ IDEA.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Cloning the Repository](#cloning-the-repository)
- [Setting Up the Project](#setting-up-the-project)
    - [Installing Java 17](#installing-java-17)
    - [Opening the Project in IntelliJ IDEA](#opening-the-project-in-intellij-idea)
    - [Setting the Java Version in IntelliJ IDEA](#setting-the-java-version-in-intellij-idea)
- [Running the Project](#running-the-project)
- [Testing](#testing)
- [Contributing](#contributing)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

Ensure the following software is installed on your machine:

- [Git](https://git-scm.com/downloads) - For cloning the repository
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) - Required to run the Spring Boot application
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) - The preferred IDE for development

---

## Cloning the Repository

1. Open your terminal (or Git Bash).
2. Navigate to the directory where you want to clone the repository.
3. Run the following command:

   ```bash
   git clone https://github.com/akshay-gurujifoundation/adolescent-development-program-be.git


## Setting Up the Project

### Installing Java 17

To run this Spring Boot application, Java 17 is required. You can download and install Java 17 from the official Oracle website or use SDKMAN to manage Java versions.

- **Option 1: Install from Oracle Website**
    1. Download Java 17 from [here](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
    2. Follow the installation steps for your operating system.

- **Option 2: Install Using SDKMAN**
    1. Install SDKMAN by running the following in your terminal:

       ```bash
       curl -s "https://get.sdkman.io" | bash
       ```

    2. Install Java 17 using SDKMAN:

       ```bash
       sdk install java 17.0.0-open
       ```

    3. Verify the installation:

       ```bash
       java -version
       ```

### Opening the Project in IntelliJ IDEA

1. Launch IntelliJ IDEA.
2. From the welcome screen, select **"Open"**.
3. Browse to the directory where the project was cloned, select the `adolescent-development-program-be` folder, and click **"Open"**.
4. IntelliJ will automatically detect the project as a Maven project and start indexing it.

### Setting the Java Version in IntelliJ IDEA

1. In IntelliJ IDEA, go to **File** > **Project Structure**.
2. Under **Project SDK**, click **New** and select **JDK**.
3. Browse to the location where Java 17 is installed, and select the JDK.
4. Apply and close the Project Structure window.

---

## Running the Project

Once the project is open and Java 17 is set:

1. Wait for Maven to resolve all dependencies (you can view the progress in the bottom right of IntelliJ IDEA).
2. Locate the `AdolescentDevelopmentProgramApplication.java` file in the `src/main/java` directory.
3. Right-click on the `AdolescentDevelopmentProgramApplication.java` file and select **"Run 'AdolescentDevelopmentProgramApplication'"**.

Alternatively, you can run the project from the terminal:

```bash
./mvnw spring-boot:run
```

### Running the Project in IntelliJ

1. In the **Run** menu, select **Run...**.
2. Select the `AdolescentDevelopmentProgramApplication` class, and the application will start on your default port (usually 8080).
3. Access the application at `http://localhost:8080`.

---

## Testing

The project includes unit and integration tests. You can run the tests using:

```bash
./mvnw test
```

Alternatively, you can run the tests from IntelliJ by right-clicking on the `src/test/java` directory and selecting **"Run Tests"**.

---

## Contributing

We welcome contributions to the Adolescent Development Program backend. To contribute:

1. Fork the repository.
2. Create a new branch with your feature or bug fix.
3. Push the branch to your fork.
4. Create a pull request, explaining your changes in detail.

---

## Troubleshooting

### Common Issues

1. **Port Conflict**: If the application fails to start due to a port conflict, change the port in the `application.properties` file by adding:

   ```properties
   server.port=8081
   ```

2. **Dependency Issues**: Ensure Maven dependencies are properly set up. If you face dependency resolution issues, run:

   ```bash
   ./mvnw clean install
   ```

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
```