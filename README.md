# SEBRAE-CONSOLIDA-EE

A Java application designed to automate event consolidation processes using Selenium WebDriver.

## Prerequisites

- Java 21
- Maven
- Chrome Browser

## Technologies

- Java 21
- Selenium WebDriver
- Apache POI
- Jackson
- WebDriverManager
- dotenv-java

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── luanmxz/
│               ├── adapters/
│               │   └── WebDriverHandler.java
│               ├── enums/
│               │   └── EventStatusEnum.java
│               ├── pages/
│               │   ├── DetailEventPage.java
│               │   ├── EventPage.java
│               │   └── LoginPage.java
│               └── services/
│                   ├── EventFileService.java
│                   └── EventProcessor.java
```

## Configuration

1. Create a `.env` file in the project root with:

```env
FILE_PATH=your_excel_file_path
```

## Building

```bash
mvn clean package
```

## Running

```bash
java -jar target/SEBRAE-CONSOLIDA-EE-1.0-SNAPSHOT.jar
```

## Features

- Multi-threaded event processing
- Excel file integration
- Automated browser interaction
- Status tracking (CONSOLIDADO, DISPONÍVEL, FALHA)
- Concurrent event processing with thread management
- Automatic WebDriver configuration

## Error Handling

The application includes comprehensive error handling:
- Browser automation failures
- File processing errors
- Thread interruptions
- Event consolidation issues

## License

This project is proprietary and confidential.
