package com.example.ms_operations.facade;

import com.example.ms_operations.model.pojo.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

  public Vehicle getVehicle(Long vehicleId) {
    log.info("Getting vehicle information for id: {}", vehicleId);
    try {
      String url = String.format(vehiclesServiceUrl, "id/" + vehicleId);
      Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);
      return vehicle;
    } catch (HttpStatusCodeException e) {
      throw e;
    }
  }

  public Vehicle updateVehicleStatus(Long vehicleId, Boolean status) {
    try {
      String url = String.format(vehiclesServiceUrl, "id/" + vehicleId + "/status/" + status);
      log.info("Calling update status endpoint: {}", url);
      ResponseEntity<Vehicle> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.PUT, null, Vehicle.class);
      return response.getBody();
    } catch (HttpStatusCodeException e) {
      log.error("Http error {} updating vehicle status for id: {}", e.getMessage(), vehicleId);
      throw e;
    }
  }
}
