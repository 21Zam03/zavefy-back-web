package com.example.ventas_bodega.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YoloDetectResponse {

    private int total;
    private Map<String, Integer> counts;
    private List<YoloDetectionResponse> detections;

}
