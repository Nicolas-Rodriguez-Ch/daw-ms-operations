package com.example.ms_operations.service;

import com.example.ms_operations.facade.VehiclesFacade;
import com.example.ms_operations.model.pojo.Vehicle;
import com.example.ms_operations.model.request.VehicleRequest;
import com.example.ms_operations.model.response.ReservationResponse;
import com.example.ms_operations.validator.VehicleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
  private final VehiclesFacade vehiclesFacade;

  @Override
  public ReservationResponse createReservation(VehicleRequest request) {
    Vehicle fetchedVehicle = vehiclesFacade.getVehicle(request.getVehicle().getId());
    VehicleValidator.validateForReservation(fetchedVehicle);
    Vehicle updatedVehicle = vehiclesFacade.updateVehicleStatus(request.getVehicle().getId(), false);
    return new ReservationResponse("Reservation created successfully", updatedVehicle);
  }

  @Override
  public ReservationResponse updateReservation(VehicleRequest request) {
    Vehicle fetchedVehicle = vehiclesFacade.getVehicle(request.getVehicle().getId());
    VehicleValidator.validateForUpdate(fetchedVehicle);
    Vehicle updatedVehicle = vehiclesFacade.updateVehicleStatus(request.getVehicle().getId(), request.getVehicle().getStatus());
    return new ReservationResponse("Availability updated successfully", updatedVehicle);
  }

  @Override
  public ReservationResponse cancelReservation(VehicleRequest request) {
    Vehicle fetchedVehicle = vehiclesFacade.getVehicle(request.getVehicle().getId());
    VehicleValidator.validateForCancellation(fetchedVehicle);
    Vehicle updatedVehicle = vehiclesFacade.updateVehicleStatus(request.getVehicle().getId(), true);
    return new ReservationResponse("Reservation cancelled successfully", updatedVehicle);
  }
}
