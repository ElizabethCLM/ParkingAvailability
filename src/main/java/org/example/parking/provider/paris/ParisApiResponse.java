package org.example.parking.provider.paris;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParisApiResponse(
        @JsonProperty("total_count") Integer total,
        List<ParisParkingResponse> results
) {}
