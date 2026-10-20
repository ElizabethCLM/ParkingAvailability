package org.example.parking.dto;

import java.time.Instant;

public record ParkingDto(
        String id,
        String name,
        Integer totalSpaces,
        Integer availableSpaces,
        Double latitude,
        Double longitude,
        Instant lastUpdated
) {
}
