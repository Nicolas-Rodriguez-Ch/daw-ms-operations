# Microservice Implementation Guide

## Overview
This microservice handles vehicle reservations by delegating operations to another microservice (ms-vehicles) via REST calls. It provides three main operations:
1. **Create Reservation** - Reserve a vehicle
2. **Update Availability** - Update vehicle availability status
3. **Cancel Reservation** - Cancel an existing reservation

## Architecture

### Components

#### 1. **ReservationsImpl.java** (Service Layer)
- Implements the `Reservations` interface
- Uses `RestTemplate` (Spring's HTTP client) to communicate with the other microservice
- Handles all business logic for reservations
- Configured as a Spring `@Service` component for dependency injection

**Key Methods:**
- `createReservation()` - Calls POST `/api/vehicles/reserve`
- `updateReservation()` - Calls POST `/api/vehicles/update-availability`
- `cancelReservation()` - Calls POST `/api/vehicles/cancel`

#### 2. **ReservationController.java** (Controller Layer)
- REST controller exposed at `/api/reservations`
- Provides endpoints for clients to interact with the service
- Handles HTTP requests and returns appropriate responses
- Includes error handling with proper HTTP status codes

**Available Endpoints:**
- `POST /api/reservations/create` - Create a new reservation
- `POST /api/reservations/update-availability` - Update vehicle availability
- `POST /api/reservations/cancel` - Cancel a reservation

#### 3. **ReservationResponse.java** (Response Model)
- Data transfer object for API responses
- Contains fields: `success`, `message`, `data`
- Makes API responses more structured and professional

## How It Works

### Request Flow
```
Client Request
    ↓
ReservationController (receives HTTP request)
    ↓
Reservations Service (ReservationsImpl)
    ↓
RestTemplate (makes HTTP call to other microservice)
    ↓
Other Microservice (ms-vehicles)
    ↓
Database (handled by other microservice)
```

### Example Request/Response

**Create Reservation:**
```
POST /api/reservations/create
Content-Type: application/json

{
  "reservations": ["1", "2", "3"]
}

Response:
200 OK
"Reservation created successfully"
```

## Configuration Notes

### Service URL
The microservice URL is defined in `ReservationsImpl.java`:
```java
private static final String VEHICLE_SERVICE_URL = "http://ms-vehicles";
```

**Update this based on your setup:**
- If using Eureka (service discovery): Keep it as `http://ms-vehicles` (service name)
- If using direct IP: Change to `http://localhost:8080` (or your IP/port)
- The `@LoadBalanced` annotation on `RestTemplate` handles service discovery automatically

### Dependencies
The project includes:
- Spring Boot Web MVC starter
- Spring Cloud Eureka Client (for service discovery)
- Lombok (for reducing boilerplate)
- RestTemplate (configured with load balancing)

## Error Handling
All service methods include try-catch blocks to handle:
- Network timeouts
- Service unavailability
- Invalid responses
- Serialization errors

Errors return appropriate messages that are helpful for debugging.

## What the Other Microservice Should Provide

The `ms-vehicles` microservice should expose these endpoints:

### 1. Create Reservation
```
POST /api/vehicles/reserve
Content-Type: application/json
Body: {"reservations": ["1", "2", "3"]}
Response: Success message or error
```
- **Logic Expected**: Check vehicle availability and mark as unavailable if available

### 2. Update Availability
```
POST /api/vehicles/update-availability
Content-Type: application/json
Body: {"reservations": ["1", "2", "3"]}
Response: Success message or error
```
- **Logic Expected**: Update the `available` flag in the Vehicle entity

### 3. Cancel Reservation
```
POST /api/vehicles/cancel
Content-Type: application/json
Body: {"reservations": ["1", "2", "3"]}
Response: Success message or error
```
- **Logic Expected**: Mark vehicles as available again (reverse the reservation)

## Next Steps

1. **Implement the ms-vehicles microservice** with the endpoints above
2. **Update the service URL** in ReservationsImpl if needed
3. **Add request validation** (check if reservations list is empty)
4. **Add logging** using Spring's Logger for debugging
5. **Add proper exception handling** with custom exceptions
6. **Consider adding dates** to ReservationRequest when needed:
   ```java
   private LocalDateTime startDate;
   private LocalDateTime endDate;
   ```
7. **Add persistence layer** if this microservice needs to store reservation data
8. **Add unit tests** for the service layer
9. **Configure application.yaml** with Eureka settings if using service discovery

## Testing the API

You can test using tools like Postman or curl:

```bash
# Create a reservation
curl -X POST http://localhost:8080/api/reservations/create \
  -H "Content-Type: application/json" \
  -d '{"reservations": ["1", "2"]}'

# Update availability
curl -X POST http://localhost:8080/api/reservations/update-availability \
  -H "Content-Type: application/json" \
  -d '{"reservations": ["1", "2"]}'

# Cancel reservation
curl -X POST http://localhost:8080/api/reservations/cancel \
  -H "Content-Type: application/json" \
  -d '{"reservations": ["1"]}'
```

## Useful Java/Spring Concepts Explained

- **@Service**: Marks the class as a service component (Spring will manage its lifecycle)
- **@Autowired**: Injects dependencies automatically
- **RestTemplate**: Uses HTTP to call other services
- **@PostMapping**: Handles POST HTTP requests
- **@RequestBody**: Automatically converts JSON to Java objects
- **ResponseEntity**: Wraps responses with status codes and headers
- **try-catch**: Handles errors gracefully

