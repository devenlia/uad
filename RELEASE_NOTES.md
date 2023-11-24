# Release Notes

## Version 1.2.1

### New Features and Enhancements
- Added the possibility to use authentication when connecting to the MongoDB database.

---

## Version 1.1.0

### New Features and Enhancements
- Dockerfile has been updated to change image base from openjdk to eclipse-temurin, improving performance. Layer separation was introduced for better image caching. A non-root user was added for enhanced security and ENTRYPOINT was updated to run Jar through Spring Boot's JarLauncher for finer control.
- Created run configurations "Load ENV-Variables", "uad dev", "Run Dockerfile", and "Dev Compose". These facilitate different execution scenarios for the project.
- Added a new dev-compose.yml file for the local development setup, which runs the latest MongoDB image and establishes connection through specified ports.
- Added a new README.md file that includes comprehensive API documentation for PageController, ContainerController, CategoryController, and LinkController.
- Added a new GitHub actions workflow for building and pushing Docker images upon push events to the 'main' branch.

### Bug Fixes and Other Changes
- The methods in CategoryService and CategoryController were renamed to be more intuitive, making the code cleaner and easier to understand.
- Unused imports, `Page` and `ReqCategory`, in the `CategoryController` class were removed, enhancing the readability and maintainability of the code.
- The service layer of the Category Service now uses a repository directly instead of an indirect service.
- The `getHomePage` method was renamed to `deletePage` in the `PageController` class to accurately reflect its functionality.
- The version in build.gradle was incremented from 1.0.0 to 1.0.1.
- MongoDB properties were removed from general and dev configurations, and a production configuration file was added.
- The .gitignore file has been updated to ignore all .env files to prevent local environment variables, potentially containing sensitive data, from being pushed to the repository.
