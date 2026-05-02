package com.example.ms_operations.facade;

import com.example.ms_operations.model.request.VehicleRequest;
import com.example.ms_operations.model.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehiclesFacade {

  @Value("${vehicles-service.url}")
  private String vehiclesServiceUrl;

  private final RestTemplate restTemplate;

  public ReservationResponse reserve(VehicleRequest request) {
    try {
      String url = String.format(vehiclesServiceUrl, "reserve");
      log.info("Calling reserve endpoint: {}", url);
      String response = restTemplate.postForObject(url, request, String.class);
      String message = response != null ? response : "Reservation created successfully";
      return new ReservationResponse(message, request.getVehicle());
    } catch (HttpStatusCodeException e) {
      log.error("Http Error {}, vehicle with id: {}", e.getStatusCode(), request.getVehicle().getId());
      String message = "Error creating reservation: " + e.getMessage();
      return new ReservationResponse(message, request.getVehicle());
    }
  }

  public ReservationResponse update(VehicleRequest request) {
    try {
      String url = String.format(vehiclesServiceUrl, "update");
      log.info("Calling update endpoint: {}", url);
      String response = restTemplate.postForObject(url, request, String.class);
      String message = response != null ? response : String.format("Availability for vehicle with id: %d updated successfully", request.getVehicle().getId());
      return new ReservationResponse(message, request.getVehicle());
    } catch (HttpStatusCodeException e) {
      log.error("Http error {} updating the availability of vehicle with id: {}", e.getMessage(), request.getVehicle().getId());
      String message = String.format("Error %s updating availability of vehicle with id: %d", e.getMessage(), request.getVehicle().getId());
      return new ReservationResponse(message, request.getVehicle());
    }
  }

  public ReservationResponse cancel(VehicleRequest request) {
    try {
      String url = String.format(vehiclesServiceUrl, "cancel");
      log.info("Calling cancel endpoint: {}", url);
      String response = restTemplate.postForObject(url, request, String.class);
      String message = response != null ? response : String.format("Reservation for vehicle with id: %d cancelled successfully", request.getVehicle().getId());
      return new ReservationResponse(message, request.getVehicle());
    } catch (HttpStatusCodeException e) {
      log.error("Http Error {} cancelling reservation for vehicle with id: {}", e.getMessage(), request.getVehicle().getId());
      String message = String.format("Error %s cancelling request of vehicle with id: %d", e.getMessage(), request.getVehicle().getId());
      return new ReservationResponse(message, request.getVehicle());
    }
  }
}

