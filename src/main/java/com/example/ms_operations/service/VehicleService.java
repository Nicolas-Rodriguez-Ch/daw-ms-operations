package com.example.ms_operations.service;

import com.example.ms_operations.model.request.VehicleRequest;

public interface VehicleService {
  String createReservation(VehicleRequest request);
  String updateReservation(VehicleRequest request);
  String cancelReservation(VehicleRequest request);
}

