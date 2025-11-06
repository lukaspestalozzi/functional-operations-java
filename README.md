# Functional Operations Java

[![CI](https://github.com/lukaspestalozzi/functional-operations-java/actions/workflows/ci.yml/badge.svg)](https://github.com/lukaspestalozzi/functional-operations-java/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/lukaspestalozzi/functional-operations-java/branch/main/graph/badge.svg)](https://codecov.io/gh/lukaspestalozzi/functional-operations-java)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A Java library providing functional operations for Java 17 and above, designed with modern functional programming practices and built using test-driven development (TDD).

## Features

- **Java 17+**: Built for modern Java with support for the latest language features
- **Functional Programming**: Provides functional utilities and operations
- **Test-Driven Development**: Built following TDD principles with comprehensive test coverage
- **High Code Quality**: Enforces 80% code coverage minimum
- **Well Documented**: Comprehensive JavaDoc documentation

## Requirements

- Java 17 or higher
- Maven 3.8.1 or higher (or use the included Maven wrapper)

## Building from Source

The project includes a Maven wrapper, so you don't need to install Maven separately.

### Unix/Linux/macOS

```bash
./mvnw clean install
```

### Windows

```cmd
mvnw.cmd clean install
```

## Running Tests

Run all tests:

```bash
./mvnw test
```

Run tests with coverage report:

```bash
./mvnw verify
```

Coverage reports are generated in `target/site/jacoco/index.html`.

## Using the Library

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.lukaspestalozzi</groupId>
    <artifactId>functional-operations-java</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Development

### Project Structure

```
functional-operations-java/
├── src/
│   ├── main/
│   │   ├── java/           # Production code
│   │   └── resources/      # Production resources
│   └── test/
│       ├── java/           # Test code
│       └── resources/      # Test resources
├── .github/
│   └── workflows/          # GitHub Actions CI/CD
├── .mvn/                   # Maven wrapper configuration
├── mvnw                    # Maven wrapper script (Unix)
├── mvnw.cmd                # Maven wrapper script (Windows)
└── pom.xml                 # Maven configuration
```

### Test-Driven Development

This project follows TDD principles:

1. **Write tests first**: Before implementing any feature, write failing tests that define the expected behavior
2. **Make tests pass**: Write the minimum code necessary to make the tests pass
3. **Refactor**: Improve the code while keeping tests green

### Code Coverage

The project enforces a minimum code coverage of 80%. The build will fail if coverage drops below this threshold.

To check coverage:

```bash
./mvnw verify
```

### Code Quality

The project uses:

- **JUnit 5**: Modern testing framework
- **AssertJ**: Fluent assertion library for readable tests
- **Mockito**: Mocking framework for unit tests
- **JaCoCo**: Code coverage tool

### CI/CD

The project uses GitHub Actions for continuous integration:

- **CI Workflow**: Runs on every push and pull request
  - Tests on multiple OS (Ubuntu, Windows, macOS)
  - Tests on multiple Java versions (17, 21)
  - Generates and uploads coverage reports
  - Enforces code quality standards

- **Release Workflow**: Triggered on GitHub releases
  - Builds and packages the library
  - Uploads release artifacts

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Write tests for your feature (TDD approach)
4. Implement your feature
5. Ensure all tests pass (`./mvnw verify`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Authors

- Lukas Pestalozzi - [lukaspestalozzi](https://github.com/lukaspestalozzi)

## Acknowledgments

- Built with Java 17+ features
- Follows modern functional programming principles
- Developed using test-driven development methodology