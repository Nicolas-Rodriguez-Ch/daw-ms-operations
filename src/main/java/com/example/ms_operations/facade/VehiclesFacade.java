package com.example.ms_operations.facade;

import com.example.ms_operations.model.request.VehicleRequest;
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

  public String reserve(VehicleRequest request) {
    try {
      String url = String.format(vehiclesServiceUrl, "reserve");
      log.info("Calling reserve endpoint: {}", url);
      String response = restTemplate.postForObject(url, request, String.class);
      return response != null ? response : "Reservation created successfully";
    } catch (HttpStatusCodeException e) {
      log.error("Http Error {}, vehicle with id: {}", e.getStatusCode(), request.getVehicle().getId());
      return "Error creating reservation: " + e.getMessage();
    }
  }

  public String update(VehicleRequest request) {
    try {
      String url = String.format(vehiclesServiceUrl, "update");
      log.info("Calling update endpoint: {}", url);
      String response = restTemplate.postForObject(url, request, String.class);
      return response != null ? response : String.format("Availability for vehicle with id: %d updated successfully", request.getVehicle().getId());
    } catch (HttpStatusCodeException e) {
      log.error("Http error {} updating the availability of vehicle with id: {}", e, request.getVehicle().getId());
      return String.format("Error %s updating availability of vehicle with id: %d", e, request.getVehicle().getId());
    }
  }

  public String cancel(VehicleRequest request) {
    try {
      String url = String.format(vehiclesServiceUrl, "cancel");
      log.info("Calling cancel endpoint: {}", url);
      String response = restTemplate.postForObject(url, request, String.class);
      return response != null ? response : String.format("Reservation for vehicle with id: %d cancelled successfully", request.getVehicle().getId());
    } catch (HttpStatusCodeException e) {
      log.error("Http Error {} cancelling reservation for vehicle  with id: {}", e.getMessage(), request.getVehicle().getId());
      return String.format("Error %s cancelling request of vehicle with id: %d", e, request.getVehicle().getId());
    }
  }
}

