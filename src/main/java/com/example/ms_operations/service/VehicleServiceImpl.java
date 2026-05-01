package com.example.ms_operations.service;

import com.example.ms_operations.model.request.VehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${vehicles-service.url}")
    private String vehiclesServiceUrl;

    @Override
    public String createReservation(VehicleRequest request) {
        try {
            String url = String.format(vehiclesServiceUrl, "reserve");
            String response = restTemplate.postForObject(url, request, String.class);
            return response != null ? response : "Reservation created successfully";
        } catch (Exception e) {
            return "Error creating reservation: " + e.getMessage();
        }
    }

    @Override
    public String updateReservation(VehicleRequest request) {
        try {
            String url = String.format(vehiclesServiceUrl, "update");
            String response = restTemplate.postForObject(url, request, String.class);
            return response != null ? response : "Availability updated successfully";
        } catch (Exception e) {
            return "Error updating availability: " + e.getMessage();
        }
    }

    @Override
    public String cancelReservation(VehicleRequest request) {
        try {
            String url = String.format(vehiclesServiceUrl, "cancel");
            String response = restTemplate.postForObject(url, request, String.class);
            return response != null ? response : "Reservation cancelled successfully";
        } catch (Exception e) {
            return "Error cancelling reservation: " + e.getMessage();
        }
    }
}

