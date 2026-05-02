package com.example.ms_operations.service;

import com.example.ms_operations.facade.VehiclesFacade;
import com.example.ms_operations.model.request.VehicleRequest;
import com.example.ms_operations.model.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
  private final VehiclesFacade vehiclesFacade;

  @Override
  public ReservationResponse createReservation(VehicleRequest request) {
    return vehiclesFacade.reserve(request);
  }

  @Override
  public ReservationResponse updateReservation(VehicleRequest request) {
    return vehiclesFacade.update(request);
  }

  @Override
  public ReservationResponse cancelReservation(VehicleRequest request) {
    return vehiclesFacade.cancel(request);
  }
}
