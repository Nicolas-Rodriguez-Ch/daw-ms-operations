package com.example.ms_operations.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Vehicle {
  private Long id;
  @JsonProperty(required = false)
  private String model;
  @JsonProperty(required = false)
  private String brand;
  @JsonProperty(required = false)
  private Boolean available;
}
