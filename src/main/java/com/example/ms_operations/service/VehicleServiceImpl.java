package com.example.ms_operations.service;

import com.example.ms_operations.facade.VehiclesFacade;
import com.example.ms_operations.model.request.VehicleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
  @Autowired
  private VehiclesFacade vehiclesFacade;

  @Override
  public String createReservation(VehicleRequest request) {
    return vehiclesFacade.reserve(request);
  }

  @Override
  public String updateReservation(VehicleRequest request) {
    return vehiclesFacade.update(request);
  }

  @Override
  public String cancelReservation(VehicleRequest request) {
    return vehiclesFacade.cancel(request);
  }
}
