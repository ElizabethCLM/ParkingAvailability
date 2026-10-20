package org.example.parking.provider.paris;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParisParkingResponse(
        String id,
        @JsonProperty("nom") String name,
        @JsonProperty("nb_places") Integer capacity,
        @JsonProperty("geo_point_2d") GeoPoint geoPoint,
        @JsonProperty("mis_a_jour") String lastUpdated
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GeoPoint(
            Double lat,
            Double lon
    ) {}
}
