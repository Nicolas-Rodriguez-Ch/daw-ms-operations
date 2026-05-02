package com.example.ms_operations.model.response;

import com.example.ms_operations.model.pojo.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
  private String message;
  private Vehicle vehicle;
}
