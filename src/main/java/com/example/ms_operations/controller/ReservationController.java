package com.example.ms_operations.controller;

import com.example.ms_operations.model.request.VehicleRequest;
import com.example.ms_operations.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ReservationController {

  @Autowired
  private VehicleService vehicleService;

  @PostMapping("/create-reservation")
  public ResponseEntity<String> createReservation(@RequestBody VehicleRequest request) {
    log.info("Reservation request received for vehicle with ID {}", request.getVehicle().getId());
    try {
      String result = vehicleService.createReservation(request);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error creating reservation: " + e.getMessage());
    }
  }

  @PostMapping("/update-availability")
  public ResponseEntity<String> updateAvailability(@RequestBody VehicleRequest request) {
    log.info("Update availability request received for vehicle with ID {}", request.getVehicle().getId());

    try {
      String result = vehicleService.updateReservation(request);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error updating availability: " + e.getMessage());
    }
  }

  @PostMapping("/cancel-reservation")
  public ResponseEntity<String> cancelReservation(@RequestBody VehicleRequest request) {
    log.info("Cancel reservation request received for vehicle with ID {}", request.getVehicle().getId());
    try {
      String result = vehicleService.cancelReservation(request);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error cancelling reservation: " + e.getMessage());
    }
  }
}

