# Parking Availability API

A REST API built with **Java 21** and **Spring Boot 3.2.2** to retrieve parking information from different cities through a unified interface.

The application currently integrates two public data sources:

- **Grand Poitiers:** Real-time parking availability, including total capacity and available spaces.
- **Paris Open Data:** Parking facility information, including total capacity, location and dataset update timestamps. Real-time availability is not provided by this dataset.

The project uses an extensible architecture that allows additional cities to be integrated without modifying the existing controller or service logic.

## 1. Technologies

- Java 21
- Spring Boot 3.2.2
- Maven
- Spring Web / RestClient
- Springdoc OpenAPI 3 / Swagger UI
- JUnit 5
- AssertJ
- Jackson

## 2. Features

- Retrieve parking information by city through a single REST endpoint.
- Fetch real-time parking availability from the Grand Poitiers public API.
- Retrieve parking facility information from the Paris Open Data API.
- Convert different external API responses into a common `ParkingDto` representation.
- Validate geographic coordinates before returning them.
- Support additional cities without modifying the controller or service logic.
- Handle unsupported cities and external API errors.
- Document and test the REST API through Swagger UI.
- Provide unit tests using saved JSON fixtures to avoid relying on external services during testing.

## 3. Architecture

The application follows a layered architecture and uses the **Strategy design pattern** to support multiple parking data providers.

```text
ParkingController
       |
       v
ParkingService
       |
       v
ParkingProvider (interface)
       |
       +--------------------------+
       |                          |
       v                          v
PoitiersParkingProvider     ParisParkingProvider
       |                          |
       v                          v
PoitiersParkingMapper       ParisParkingMapper
       |                          |
       +------------+-------------+
                    |
                    v
                ParkingDto
```

### Strategy Pattern

The `ParkingProvider` interface defines the contract that every city-specific provider must implement:

```java
public interface ParkingProvider {
    String getCity();
    List<ParkingDto> getParkings();
}
```

Each concrete provider implements its own logic for retrieving parking information:

- **PoitiersParkingProvider:** Connects to the Grand Poitiers real-time parking API.
- **ParisParkingProvider:** Connects to the Paris Open Data API.

Spring automatically injects the available `ParkingProvider` implementations into `ParkingService`, which selects the appropriate provider based on the `city` parameter.

This approach follows the **Open/Closed Principle**: the application can be extended with new providers without modifying the existing controller or service.

### Responsibilities

| Component | Responsibility |
|---|---|
| `ParkingController` | Exposes the REST API |
| `ParkingService` | Selects the provider for the requested city |
| `ParkingProvider` | Defines the common provider contract |
| `PoitiersParkingProvider` | Retrieves real-time parking information from Grand Poitiers |
| `PoitiersParkingMapper` | Converts Poitiers API records into `ParkingDto` |
| `ParisParkingProvider` | Retrieves parking facility information from Paris Open Data |
| `ParisParkingMapper` | Converts Paris API records into `ParkingDto` |
| `CoordinateValidator` | Validates geographic coordinates |
| `GlobalExceptionHandler` | Handles application and external API errors |

## 4. Data Sources

### Grand Poitiers — Real-Time Parking API

The Poitiers integration uses a public API providing:

- Parking identifiers and names
- Total parking capacity
- Currently available spaces
- Geographic coordinates
- Last update timestamps

**API endpoint:**

https://data.grandpoitiers.fr/data-fair/api/v1/datasets/mobilites-stationnement-des-parkings-en-temps-reel/lines

### Paris — Open Data Parking API

The Paris integration uses the official Paris Open Data platform.

**API endpoint:**

https://opendata.paris.fr/api/explore/v2.1/catalog/datasets/stationnement-en-ouvrage/records?limit=20

The dataset provides:

- Parking identifiers and names
- Total parking capacity (`nb_places`)
- Geographic coordinates (`geo_point_2d`)
- Dataset update timestamps (`mis_a_jour`)

**Important limitation:** This dataset does not provide real-time parking availability. Consequently, the API returns `null` for `availableSpaces` when the requested city is Paris.

This is intentional: `null` represents unavailable information, whereas `0` would incorrectly indicate that the parking facility is full.

The Paris endpoint currently retrieves up to 20 records per request. Pagination would be needed to retrieve the complete dataset.

## 5. REST API

### Endpoint

```http
GET /api/parkings?city={city}
```

### Supported Cities

| City | Data source | Real-time availability |
|---|---|---|
| `poitiers` | Grand Poitiers | Yes |
| `paris` | Paris Open Data | No |

City names are case-insensitive. For example, both `paris` and `Paris` are accepted.

### Example: Poitiers

```http
GET http://localhost:8080/api/parkings?city=poitiers
```

Example response structure:

```json
[
  {
    "id": "poitiers-3",
    "name": "THEATRE",
    "totalSpaces": 320,
    "availableSpaces": 99,
    "latitude": 46.58383455409422,
    "longitude": 0.33779491061805567,
    "lastUpdated": "2026-10-19T12:08:17Z"
  }
]
```

Values are illustrative and may change as the external API is updated.

### Example: Paris

```http
GET http://localhost:8080/api/parkings?city=paris
```

Example response structure:

```json
[
  {
    "id": "paris-75107-P-060",
    "name": "INVALIDES",
    "totalSpaces": 894,
    "availableSpaces": null,
    "latitude": 48.859287581418464,
    "longitude": 2.3146599819354683,
    "lastUpdated": "2026-06-01T02:00:00Z"
  }
]
```

For Paris, `lastUpdated` represents the dataset record's update timestamp, not a real-time occupancy measurement.

### Error Handling

The application handles common error scenarios:

| HTTP status | Description |
|---|---|
| `200 OK` | Parking information retrieved successfully |
| `400 Bad Request` | Unsupported city |
| `502 Bad Gateway` | Error retrieving or processing external API data |

## 6. Running the Application

### Prerequisites

- Java 21
- Maven
- Internet access to retrieve parking data from the external APIs

### Option 1: Run from IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Wait for Maven to download and load the project dependencies.
3. Navigate to the main application class:

   `src/main/java/org/example/ParkingApplication.java`

4. Open `ParkingApplication.java`, which contains the Spring Boot entry point:

   ```java
   package org.example;

   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;

   @SpringBootApplication
   public class ParkingApplication {

       public static void main(String[] args) {
           SpringApplication.run(ParkingApplication.class, args);
       }
   }
   ```

5. Click the green **Run** button next to the `main()` method.
6. Wait for the application to start successfully.

The application runs on port `8080` by default.

### Option 2: Run from the Terminal

From the project root directory, where `pom.xml` is located, execute:

```bash
mvn spring-boot:run
```

Alternatively, build and run the application using Maven:

```bash
mvn clean package
java -jar target/*.jar
```

### Access the REST API

Once the application is running, use either endpoint:

**Poitiers:**

http://localhost:8080/api/parkings?city=poitiers

**Paris:**

http://localhost:8080/api/parkings?city=paris

Example using cURL:

```bash
curl "http://localhost:8080/api/parkings?city=poitiers"
```

```bash
curl "http://localhost:8080/api/parkings?city=paris"
```

## 7. Swagger UI and OpenAPI Documentation

Swagger UI starts automatically with the Spring Boot application. No separate server or command is required.

**Swagger UI:**

http://localhost:8080/swagger-ui.html

**Alternative Swagger UI URL:**

http://localhost:8080/swagger-ui/index.html

**OpenAPI 3 JSON documentation:**

http://localhost:8080/v3/api-docs

### Testing the API Using Swagger

1. Start the Spring Boot application.
2. Open Swagger UI in your browser.
3. Expand `GET /api/parkings`.
4. Click **Try it out**.
5. Enter `poitiers` or `paris` in the `city` parameter.
6. Click **Execute**.
7. Inspect the HTTP response and the parking data returned by the API.

For Poitiers, the response includes available parking spaces.
![poitiers_swagger.png](screenshots/poitiers_swagger.png)

For Paris, the response includes parking capacity and location information, with `availableSpaces` set to `null`.
![paris_swagger.png](screenshots/paris_swagger.png)
## 8. Testing

The project uses **JUnit 5**, **AssertJ** and **Jackson**.

Tests cover:

- Mapping external parking records into `ParkingDto`
- Geographic coordinate validation
- Missing or invalid coordinates
- Missing timestamps
- JSON deserialization using saved API responses
- City-specific data mapping

JSON fixtures are stored under:

```text
src/test/resources/parking_json/
├── poitiers-parkings.json
└── paris-parkings.json
```

These fixtures make deserialization tests deterministic and independent of external API availability.

### Run All Tests

```bash
mvn test
```

## 9. Adding a New City

The application is designed to support additional cities with minimal changes.

For example, to add Cannes:

1. Create a `CannesParkingProvider` implementing `ParkingProvider`.
2. Create response models matching the external API's JSON structure.
3. Create a `CannesParkingMapper` to convert external records into `ParkingDto`.
4. Configure the external API URL in `application.properties`.
5. Add unit tests for the provider and mapper.

Spring will automatically discover the new provider through dependency injection.

The existing `ParkingController` and `ParkingService` do not need to be modified.

## 10. Limitations and Possible Improvements

- **Paris availability:** The current Paris Open Data dataset does not expose real-time available spaces.
- **Pagination:** The Paris API currently retrieves a limited number of records per request.
- **External dependencies:** Live requests depend on the availability of third-party APIs.
- **Caching:** A short-lived cache could reduce repeated external API calls.
- **Timeouts:** Explicit HTTP connection and response timeouts could improve resilience.
- **Monitoring:** Additional logging and metrics could help diagnose upstream API failures.

## 11. Conclusion

This project demonstrates a modular Spring Boot REST API that integrates parking data from different public sources through a common interface.

The **Strategy design pattern** separates city-specific API integrations from the application’s core logic, making the system easier to maintain, test and extend.

Both Poitiers and Paris are integrated using real public APIs. Differences in the data they provide are handled transparently through the shared `ParkingDto` model, without inventing unavailable information.