package org.example.parking.provider.poitiers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PoitiersParkingResponse(
        @JsonProperty("Id")
        Integer id,

        @JsonProperty("Nom")
        String name,

        @JsonProperty("Capacite")
        Integer capacity,

        @JsonProperty("Places")
        Integer availableSpaces,

        @JsonProperty("infos_parkingsgeo_point")
        String geoPoint,

        @JsonProperty("Dernière_mise_à_jour_Base")
        String lastUpdated

) {}
