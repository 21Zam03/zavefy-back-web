package com.example.ventas_bodega.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YoloDetectionResponse {

    @JsonProperty("className")
    private String className;

    @JsonProperty("confidence")
    private Double confidence;

}
