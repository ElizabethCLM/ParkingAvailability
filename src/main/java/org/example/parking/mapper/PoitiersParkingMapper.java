package org.example.parking.mapper;

import org.example.parking.dto.ParkingDto;
import org.example.parking.helper.CoordinateValidator;
import org.example.parking.provider.poitiers.PoitiersParkingResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PoitiersParkingMapper {
    public ParkingDto toDto(PoitiersParkingResponse parking) {

        Double latitude = null;
        Double longitude = null;

        String geoPoint = parking.geoPoint();

        if (geoPoint != null && !geoPoint.isBlank()) {
            String[] coordinates = geoPoint.split(",");

            if (coordinates.length == 2) {
                try {
                    double lat = Double.parseDouble(coordinates[0].trim());
                    double lon = Double.parseDouble(coordinates[1].trim());

                    // Validate both coordinates
                    if (CoordinateValidator.isValid(lat, lon)) {
                        latitude = lat;
                        longitude = lon;
                    }

                } catch (NumberFormatException ignored) {
                    // Invalid coordinates remain null.
                }
            }
        }

        return new ParkingDto(
                "poitiers-" + parking.id(),
                parking.name(),
                parking.capacity(),
                parking.availableSpaces(),
                latitude,
                longitude,
                parking.lastUpdated() != null
                        ? Instant.parse(parking.lastUpdated())
                        : null
        );
    }
}