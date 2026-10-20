package org.example.parking.provider.poitiers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PoitiersApiResponse(
        Integer total,
        List<PoitiersParkingResponse> results
) {}
