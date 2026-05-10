package com.example.ms_operations.validator;

import com.example.ms_operations.model.pojo.Vehicle;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class VehicleValidator {

  public static void validateForReservation(Vehicle vehicle) {
    if (vehicle == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Vehicle not found");
    }

    if (!Boolean.TRUE.equals(vehicle.getStatus())) {
      throw new HttpClientErrorException(
          HttpStatus.valueOf(422),
          "Vehicle is not available for reservation");
    }
  }

  public static void validateForCancellation(Vehicle vehicle) {
    if (vehicle == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Vehicle not found");
    }

    if (Boolean.TRUE.equals(vehicle.getStatus())) {
      throw new HttpClientErrorException(
          HttpStatus.valueOf(422),
          "Cannot cancel reservation: vehicle is not currently reserved");
    }
  }

  public static void validateForUpdate(Vehicle vehicle) {
    if (vehicle == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Vehicle not found");
    }
  }
}



