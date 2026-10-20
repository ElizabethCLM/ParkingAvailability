package org.example.parking.mapper;


import org.example.parking.dto.ParkingDto;
import org.example.parking.helper.CoordinateValidator;
import org.example.parking.provider.paris.ParisParkingResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;

@Component
public class ParisParkingMapper {

    public ParkingDto toDto(ParisParkingResponse parking) {

        Double latitude = null;
        Double longitude = null;

        if (parking.geoPoint() != null) {
            Double lat = parking.geoPoint().lat();
            Double lon = parking.geoPoint().lon();

            if (CoordinateValidator.isValid(lat, lon)) {
                latitude = lat;
                longitude = lon;
            }
        }

        return new ParkingDto(
                "paris-" + parking.id(),
                parking.name(),
                parking.capacity(),
                null, // Real-time availability is not provided
                latitude,
                longitude,
                parking.lastUpdated() != null
                        ? OffsetDateTime.parse(parking.lastUpdated()).toInstant()
                        : null
        );
    }
}
