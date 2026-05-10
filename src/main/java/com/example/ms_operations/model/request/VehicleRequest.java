package com.example.ms_operations.model.request;

import com.example.ms_operations.model.pojo.Vehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for reservation management operations containing vehicle data")
public class VehicleRequest {
  @Schema(description = "Vehicle object with details for the reservation operation", requiredMode = Schema.RequiredMode.REQUIRED)
  private Vehicle vehicle;
}

