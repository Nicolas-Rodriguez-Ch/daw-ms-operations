package com.example.ms_operations.model.request;

import com.example.ms_operations.model.pojo.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {
  private Vehicle vehicle;
}

