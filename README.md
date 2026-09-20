# Knowledge Assistant

A Spring Boot application that loads a knowledge text file and exposes both keyword-based and semantic search endpoints using Spring AI and vector search.

## Overview

This project is a lightweight knowledge retrieval service built with Java and Spring Boot. It reads a local text file, indexes its content into a vector store, and makes it searchable through REST endpoints. The application is designed for scenarios where you want to query a document corpus using either simple text matching or semantic similarity.

## Features

- Loads knowledge from a configurable text file
- Supports keyword search over document chunks
- Supports semantic search using Spring AI vector search
- Built on Spring Boot 3 / 4-compatible stack
- Uses Java 21 and Maven
- Exposes REST APIs for querying knowledge

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring AI
- Spring Web
- Maven
- LangChain4j embeddings (`all-minilm-l6-v2`)

## Project Structure

```text
knowledge-assistant/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/amitkumar/ai/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── embedding/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   ├── KnowledgeAssistantApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Application Flow

- `KnowledgeAssistantApplication` bootstraps the Spring application.
- `VectorStoreConfig` creates a `VectorStore` backed by `SimpleVectorStore` and the configured embedding model.
- `KnowledgeController` exposes endpoints for knowledge retrieval and search.
- `KnowledgeService` reads the knowledge file and performs both text and semantic queries.

## Configuration

The app is configured in `src/main/resources/application.properties`:

```properties
spring.application.name=knowledge-assistant
knowledge.file.path=C:\Users\amitk\Downloads\transcript.txt
```

The `knowledge.file.path` property points to the text file that will be loaded and indexed. Update this to the location of your own knowledge source before running the app.

## Prerequisites

- Java 21+
- Maven or the bundled Maven wrapper
- A knowledge text file at the configured path

## Running the Application

### Using Maven wrapper

```bash
./mvnw spring-boot:run
```

### On Windows

```powershell
mvnw.cmd spring-boot:run
```

The application starts as a Spring Boot web service.

## API Endpoints

### 1. Load knowledge file

```http
GET /knowledge
```

Returns the raw contents of the configured knowledge file.

### 2. Search knowledge by keyword

```http
GET /search?query=your+search+terms
```

Returns matching text chunks ranked by keyword overlap.

### 3. Search knowledge semantically

```http
GET /semantic-search?query=your+search+terms
```

Returns semantic search results with document ID, text, score, and metadata.

## Example

```bash
curl "http://localhost:8080/search?query=Spring%20AI"
curl "http://localhost:8080/semantic-search?query=What%20is%20vector%20search%20in%20Spring%20AI?"
```

## Notes

- The repository currently includes a starter configuration for Spring AI vector storage but does not include a dedicated model provider configuration in the application properties. Depending on your setup, you may need to add or configure the embedding model provider for your environment.
- The project is intentionally lightweight and serves as a foundation for building a document-based assistant around a knowledge file.

## License

This project does not currently declare a license in `pom.xml`. If you plan to publish or distribute it publicly, consider adding an appropriate open-source license.

## Contributing

Contributions are welcome. If you want to improve the project, open an issue or submit a pull request with your changes.
