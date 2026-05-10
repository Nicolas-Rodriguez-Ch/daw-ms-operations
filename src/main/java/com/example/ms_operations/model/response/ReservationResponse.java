package com.example.ms_operations.model.response;

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
@Schema(description = "Response object containing the result of a reservation operation with status message and vehicle data")
public class ReservationResponse {
  @Schema(description = "Operation status message describing the outcome", example = "Reservation created successfully", requiredMode = Schema.RequiredMode.REQUIRED)
  private String message;

  @Schema(description = "Updated vehicle information after the operation", requiredMode = Schema.RequiredMode.REQUIRED)
  private Vehicle vehicle;
}
