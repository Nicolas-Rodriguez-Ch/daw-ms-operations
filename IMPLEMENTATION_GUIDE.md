# Microservice Implementation Guide

## Overview
This microservice manages vehicle rental requests by delegating all operations to the ms-vehicles microservice via REST calls. It provides three main operations using a clean separation of concerns pattern:

1. **Create Reservation** - Register a vehicle rental request
2. **Update Availability** - Update vehicle status (available/unavailable)
3. **Cancel Reservation** - Cancel an existing rental request

## Architecture

### Three-Layer Pattern

```
ReservationController (HTTP Layer)
        ↓
VehicleService (Business Logic Layer)
        ↓
VehiclesFacade (Microservice Communication Layer)
        ↓
RestTemplate → ms-vehicles microservice
```

### Components

#### 1. **VehicleService.java & VehicleServiceImpl.java** (Business Logic)
- Interface defining the three core operations
- Implementation delegates to VehiclesFacade
- Service layer knows WHAT operations exist, not HOW to perform them
- Uses `@Service` annotation for Spring management
- Uses `@RequiredArgsConstructor` for dependency injection

**Methods:**
- `createReservation(VehicleRequest)` - Delegates to facade.reserve()
- `updateReservation(VehicleRequest)` - Delegates to facade.update()
- `cancelReservation(VehicleRequest)` - Delegates to facade.cancel()

#### 2. **VehiclesFacade.java** (Microservice Communication)
- Handles ALL REST communication with ms-vehicles
- Reads configuration from `application.yaml`
- Uses RestTemplate for HTTP calls
- Catches `HttpStatusCodeException` for error handling
- Uses SLF4J (`@Slf4j`) for logging

**Methods:**
- `reserve(VehicleRequest)` - Calls POST `/vehicles/reserve`
- `update(VehicleRequest)` - Calls POST `/vehicles/update`
- `cancel(VehicleRequest)` - Calls POST `/vehicles/cancel`

**Why separate the Facade?**
- If you need to change HOW to call the other service, you only change the Facade
- If you need to change business logic, you only change the Service
- Each layer has one responsibility (Single Responsibility Principle)

#### 3. **ReservationController.java** (HTTP Layer)
- REST controller handling incoming HTTP requests
- No root path prefix (not `/api/reservations`)
- Provides simple endpoints for reservation operations
- Injects VehicleService for business logic execution

**Available Endpoints:**
- `POST /create-reservation` - Create a new reservation
- `POST /update-availability` - Update vehicle availability
- `POST /cancel-reservation` - Cancel a reservation

#### 4. **VehicleRequest.java** (Request Model)
- Contains a single `Vehicle` object (not a list)
- Why? The requirements state one reservation at a time per request
- Uses Lombok annotations for clean code

#### 5. **Vehicle.java** (Vehicle Model)
- POJO representing a vehicle
- Fields: `id`, `model`, `brand`, `available`
- Shared between ms-operations and ms-vehicles

## How It Works

### Request Flow
```
1. Client sends HTTP request to ReservationController
2. Controller receives the request and extracts VehicleRequest
3. Controller calls VehicleService method
4. Service immediately delegates to VehiclesFacade
5. Facade builds the URL using String.format()
6. Facade logs the action using SLF4J
7. Facade makes REST call using RestTemplate
8. Response comes back from ms-vehicles
9. Facade catches any HttpStatusCodeException errors
10. Response returns through Service → Controller → Client
```

### Example: Create Reservation

**Client sends:**
```json
POST /create-reservation
Content-Type: application/json

{
  "vehicle": {
    "id": 1,
    "model": "Tesla Model 3",
    "brand": "Tesla",
    "available": true
  }
}
```

**What happens internally:**
1. Controller receives request and parses JSON to VehicleRequest object
2. Controller calls `vehicleService.createReservation(request)`
3. Service calls `vehiclesFacade.reserve(request)`
4. Facade builds URL: `http://ms-vehicles/vehicles/reserve` (replacing `%s` with "reserve")
5. Facade logs: "Calling reserve endpoint: http://ms-vehicles/vehicles/reserve"
6. Facade makes POST call with the vehicle object as JSON body
7. ms-vehicles responds with success or error
8. Response returns up the chain

## Configuration

### application.yaml
Must define the vehicles service URL:
```yaml
vehicles-service:
  url: http://ms-vehicles/vehicles/%s
```

The `%s` placeholder gets replaced with:
- `reserve` → `/vehicles/reserve`
- `update` → `/vehicles/update`
- `cancel` → `/vehicles/cancel`

### Updating the URL
For different environments:
- **Eureka (Service Discovery):** `http://ms-vehicles/vehicles/%s`
- **Direct IP:** `http://192.168.1.100:8081/vehicles/%s`
- **Docker:** `http://ms-vehicles-container:8081/vehicles/%s`
- **Local development:** `http://localhost:8081/vehicles/%s`

## Error Handling

### Exception Hierarchy
- **HttpStatusCodeException** (parent class)
  - **HttpClientErrorException** (4xx errors - client's fault)
  - **HttpServerErrorException** (5xx errors - server's fault)

### What Gets Caught
The Facade catches `HttpStatusCodeException`, which handles:
- 400 Bad Request (you sent invalid data)
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found
- 500 Internal Server Error
- etc.

### What Doesn't Get Caught
Other exceptions will propagate (crash the app):
- Network timeout
- Connection refused
- Null pointer exception
- These mean something is seriously wrong, so crashing alerts you

### Error Response
When an HTTP error occurs:
```java
catch (HttpStatusCodeException e) {
  log.error("Http Error {}, vehicle with id: {}", e.getStatusCode(), request.getVehicle().getId());
  return "Error " + e.getMessage() + " for vehicle " + request.getVehicle().getId();
}
```

The error details are logged and returned to the client.

## What ms-vehicles Must Implement

Your microservice calls these endpoints. The ms-vehicles service must provide:

### 1. POST /vehicles/reserve
**Purpose:** Check vehicle availability and mark as unavailable

**Receives:**
```json
{
  "vehicle": {
    "id": 1,
    "model": "Tesla Model 3",
    "brand": "Tesla",
    "available": true
  }
}
```

**Should do:**
- Check if vehicle.available == true
- If yes, set it to false and save
- Return success message

**Returns:**
```
"Reservation created successfully" or error message
```

### 2. POST /vehicles/update
**Purpose:** Update vehicle availability status

**Receives:**
```json
{
  "vehicle": {
    "id": 1,
    "model": "Tesla Model 3",
    "brand": "Tesla",
    "available": false
  }
}
```

**Should do:**
- Update the vehicle's available flag in database
- Save changes

**Returns:**
```
"Availability for vehicle with id: 1 updated successfully" or error
```

### 3. POST /vehicles/cancel
**Purpose:** Mark vehicle as available again (reverse a reservation)

**Receives:**
```json
{
  "vehicle": {
    "id": 1,
    "model": "Tesla Model 3",
    "brand": "Tesla",
    "available": false
  }
}
```

**Should do:**
- Set available = true
- Mark the rental as cancelled in database
- Save changes

**Returns:**
```
"Reservation for vehicle with id: 1 cancelled successfully" or error
```

## Next Steps

1. **Start this microservice** and test endpoints with Postman
2. **Implement the ms-vehicles microservice** with the three endpoints above
3. **Update `application.yaml`** if ms-vehicles runs on different host/port
4. **Add input validation** to VehicleRequest (check for null vehicle, etc.)
5. **Add database persistence** if ms-operations needs to store requests
6. **Add unit tests** for VehicleService and VehiclesFacade
7. **Consider adding dates** later when requirements expand:
   ```java
   private LocalDateTime startDate;
   private LocalDateTime endDate;
   ```

## Testing with Postman

Since ms-vehicles is not ready yet, you can:

### Option 1: Use Postman Mocks
Create a mock server in Postman that simulates ms-vehicles responses.

### Option 2: Use WireMock
Create a fake HTTP server that responds like ms-vehicles.

### Option 3: Test Once ms-vehicles is Ready
For now, test that:
1. Your controller accepts requests
2. Your endpoints are accessible
3. Port/routes are correct

**Request Body for All Tests:**
```json
{
  "vehicle": {
    "id": 1,
    "model": "Tesla Model 3",
    "brand": "Tesla",
    "available": true
  }
}
```

**Endpoints to Test:**
- `POST http://localhost:{port}/create-reservation`
- `POST http://localhost:{port}/update-availability`
- `POST http://localhost:{port}/cancel-reservation`

When the other microservice is ready, your calls will work automatically!

