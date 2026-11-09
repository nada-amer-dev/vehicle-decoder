# Vehicle Decoder API

A Spring Boot 4 / Java 21 REST API that decodes vehicle VINs and stores the results in a database.

Ask for a VIN: if it is already saved, the API returns it from its own database. If not, it asks the public [NHTSA vPIC API](https://vpic.nhtsa.dot.gov/api/), saves the decoded vehicle, and returns it.

## How it works

```
Client (Postman) -> VehicleController -> VehicleService -> VehicleRepository -> H2 database
                                               |
                                               +-> NhtsaClient -> NHTSA vPIC API
```

For `GET /vehicles/{vin}`:

1. The service looks for the VIN in the local database.
2. If found, it returns the saved vehicle (no external call).
3. If not found, `NhtsaClient` decodes the VIN with NHTSA.
4. If NHTSA returns a make, model and year, the vehicle is saved and returned.
5. If NHTSA cannot decode the VIN, the API responds with `404`.

The local database works as a cache for NHTSA data. A saved vehicle is a snapshot and is not refreshed automatically.

## Tech stack

- Java 21
- Spring Boot 4.1.2 (Spring MVC, Spring Data JPA, Bean Validation)
- H2 in-memory database
- Spring `RestClient` for the NHTSA call
- Maven (wrapper included)

## Project structure

| Class | Responsibility |
| --- | --- |
| `VehicleController` | REST endpoints; receives requests and returns responses |
| `VehicleService` | Business logic: look up, decode, save, duplicate check |
| `VehicleRepository` | Spring Data JPA repository (`findByVin`) |
| `Vehicle` | JPA entity with validation rules on the VIN |
| `NhtsaClient` | The only class that talks to the NHTSA API |
| `NhtsaResponse`, `NhtsaResult` | Records that map the NHTSA JSON |
| `GlobalExceptionHandler` | Turns exceptions into clean JSON error responses |
| `VehicleNotFoundException`, `VehicleAlreadyExistsException` | Custom exceptions |

## Getting started

### Prerequisites

- JDK 21
- Internet access (for VINs that are not saved yet)

### Run

```bash
git clone https://github.com/nada-amer-dev/vehicle-decoder.git
cd vehicle-decoder
./mvnw spring-boot:run
```

On Windows, use `mvnw spring-boot:run`.

The API starts on **http://localhost:8081**.

> The H2 database is in memory, so saved vehicles are lost when the application stops.

## API

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/vehicles/{vin}` | Returns a vehicle. Decodes it with NHTSA if it is not saved yet. |
| `POST` | `/vehicles` | Saves a vehicle that you provide. |

### VIN rules

A VIN must be exactly 17 characters: digits and capital letters, without `I`, `O` or `Q` (`^[A-HJ-NPR-Z0-9]{17}$`). This rule is checked on `POST /vehicles`.

### Status codes

| Code | When |
| --- | --- |
| `200 OK` | Vehicle returned or saved |
| `400 Bad Request` | The request body failed validation |
| `404 Not Found` | The VIN is not saved and NHTSA could not decode it |
| `409 Conflict` | A vehicle with this VIN already exists (`POST`) |

## Examples

### Decode a VIN

```
GET http://localhost:8081/vehicles/1HGCM82633A004352
```

```json
{
  "id": 1,
  "vin": "1HGCM82633A004352",
  "make": "HONDA",
  "model": "Accord",
  "modelYear": 2003
}
```

Calling the same URL again returns the same vehicle from the local database.

### Save a vehicle

```
POST http://localhost:8081/vehicles
Content-Type: application/json
```

```json
{
  "vin": "4T1BF1FK5GU123456",
  "make": "Toyota",
  "model": "Camry",
  "modelYear": 2016
}
```

### Error responses

A VIN that cannot be decoded (`404`):

```json
{ "error": "Could not decode VIN: 11111111111111111" }
```

A duplicate VIN (`409`):

```json
{ "error": "VIN already exists: 4T1BF1FK5GU123456" }
```

A VIN that fails validation (`400`):

```json
{ "vin": "VIN must be 17 characters: digits and capital letters, without I, O or Q" }
```

## Design notes

- **Separation of concerns:** the controller handles HTTP, the service holds the logic, the repository handles the database, and `NhtsaClient` is the only door to the external API.
- **Constructor injection** with `final` fields, with no field injection.
- **Validation** with `@Valid` on the request body, plus a `unique` constraint on the VIN column as the last line of defense against duplicates.
- **Separate records for the NHTSA JSON**, so a change on their side does not affect the database model.

## Possible next steps

- Unit tests for `VehicleService` with a fake `NhtsaClient`
- Refresh saved vehicles after a set number of days
- Switch from H2 to PostgreSQL or MySQL for persistent storage

## Data source

Vehicle data comes from the [NHTSA vPIC API](https://vpic.nhtsa.dot.gov/api/), a free public service of the US National Highway Traffic Safety Administration.
