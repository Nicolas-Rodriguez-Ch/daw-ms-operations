package com.example.ms_operations.controller;

import com.example.ms_operations.model.request.VehicleRequest;
import com.example.ms_operations.model.response.ReservationResponse;
import com.example.ms_operations.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
@Slf4j
@Tag(name = "Reservation Management", description = "Endpoints for managing vehicle reservations, availability updates, and cancellations")
public class ReservationController {

  @Autowired
  private VehicleService vehicleService;

  @PostMapping("/create-reservation")
  @Operation(
      summary = "Create a new reservation",
      description = "Creates a new vehicle reservation. The request must contain a valid vehicle ID. Upon success, returns the updated vehicle information with reservation status."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reservation operation completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "404", description = "Vehicle not found in vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "409", description = "Conflict from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "422", description = "Unprocessable entity from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class)))
  })
  public ResponseEntity<ReservationResponse> createReservation(@RequestBody VehicleRequest request) {
    log.info("Reservation request received for vehicle with ID {}", request.getVehicle().getId());
    try {
      ReservationResponse result = vehicleService.createReservation(request);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      String message = String.format("Error %s creating request of vehicle with id: %d", e.getMessage(), request.getVehicle().getId());
      HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      if (e instanceof HttpStatusCodeException) {
        status = (HttpStatus) ((HttpStatusCodeException) e).getStatusCode();
      }

      return ResponseEntity.status(status).body(new ReservationResponse(message, request.getVehicle()));
    }
  }

  @PostMapping("/update-availability")
  @Operation(
      summary = "Update vehicle availability",
      description = "Updates the availability status of a vehicle in the reservation system. Use this endpoint to mark vehicles as available or unavailable. Returns the vehicle information with the new availability status."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Availability update operation completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "404", description = "Vehicle not found in vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "409", description = "Conflict from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "422", description = "Unprocessable entity from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class)))
  })
  public ResponseEntity<ReservationResponse> updateAvailability(@RequestBody VehicleRequest request) {
    log.info("Update availability request received for vehicle with ID {}", request.getVehicle().getId());

    try {
      ReservationResponse result = vehicleService.updateReservation(request);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      String message = String.format("Error %s updating availability of vehicle with id: %d", e.getMessage(), request.getVehicle().getId());

      HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      if (e instanceof HttpStatusCodeException) {
        status = (HttpStatus) ((HttpStatusCodeException) e).getStatusCode();
      }

      return ResponseEntity.status(status).body(new ReservationResponse(message, request.getVehicle()));
    }
  }

  @PostMapping("/cancel-reservation")
  @Operation(
      summary = "Cancel an existing reservation",
      description = "Cancels an existing vehicle reservation and marks the vehicle as available. The request must contain a valid vehicle ID from an active reservation. Upon successful cancellation, the vehicle becomes available for new reservations."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "404", description = "Vehicle not found in vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "409", description = "Conflict from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "422", description = "Unprocessable entity from vehicles service", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class)))
  })
  public ResponseEntity<ReservationResponse> cancelReservation(@RequestBody VehicleRequest request) {
    log.info("Cancel reservation request received for vehicle with ID {}", request.getVehicle().getId());
    try {
      ReservationResponse result = vehicleService.cancelReservation(request);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      String message = String.format("Error %s cancelling request of vehicle with id: %d", e.getMessage(), request.getVehicle().getId());

      HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      if (e instanceof HttpStatusCodeException) {
        status = (HttpStatus) ((HttpStatusCodeException) e).getStatusCode();
      }

      return ResponseEntity.status(status).body(new ReservationResponse(message, request.getVehicle()));
    }
  }
}

