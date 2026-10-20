package org.example.parking.service;

import org.example.parking.dto.ParkingDto;
import org.example.parking.provider.ParkingProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ParkingService {
    private final Map<String, ParkingProvider> providers;

    public ParkingService(List<ParkingProvider> parkingProviders) {

        this.providers = parkingProviders.stream()
                .collect(Collectors.toMap(
                        ParkingProvider::getCity,
                        Function.identity()
                ));
    }

    public List<ParkingDto> getParkings(String city) {

        ParkingProvider provider = providers.get(city.toLowerCase(java.util.Locale.ROOT));

        if (provider == null) {
            throw new IllegalArgumentException("Unsupported city: " + city);
        }

        return provider.getParkings();
    }
}
