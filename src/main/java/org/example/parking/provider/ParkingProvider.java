package org.example.parking.provider;

import org.example.parking.dto.ParkingDto;

import java.util.List;

public interface ParkingProvider {
    String getCity();

    List<ParkingDto> getParkings();
}
