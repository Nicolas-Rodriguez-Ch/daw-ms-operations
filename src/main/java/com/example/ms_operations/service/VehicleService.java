package com.example.ms_operations.service;

import com.example.ms_operations.model.request.VehicleRequest;
import com.example.ms_operations.model.response.ReservationResponse;

public interface VehicleService {
  ReservationResponse createReservation(VehicleRequest request);
  ReservationResponse updateReservation(VehicleRequest request);
  ReservationResponse cancelReservation(VehicleRequest request);
}

