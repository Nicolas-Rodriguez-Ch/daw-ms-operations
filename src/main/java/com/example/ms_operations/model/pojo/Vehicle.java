package com.example.ms_operations.model.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Vehicle information with identification, model, brand, and availability status")
public class Vehicle {
  @Schema(description = "Unique vehicle identifier", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
  private Long id;

  @Schema(description = "Vehicle model name", example = "Corolla", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty(required = false)
  private String model;

  @Schema(description = "Vehicle brand/manufacturer", example = "Toyota", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty(required = false)
  private String brand;

  @Schema(description = "Vehicle availability status (true = available, false = reserved)", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty(required = false)
  private Boolean available;
}
