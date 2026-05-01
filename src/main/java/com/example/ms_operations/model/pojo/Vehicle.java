package com.example.ms_operations.model.pojo;

import lombok.Data;

@Data
public class Vehicle {
  private Long id;
  private String model;
  private String brand;
  private Boolean available;
}
