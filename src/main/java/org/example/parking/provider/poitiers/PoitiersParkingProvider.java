package org.example.parking.provider.poitiers;

import org.example.parking.dto.ParkingDto;
import org.example.parking.mapper.PoitiersParkingMapper;
import org.example.parking.provider.ParkingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PoitiersParkingProvider implements ParkingProvider {
    private final RestClient restClient;
    private final String apiUrl;
    private final PoitiersParkingMapper mapper;

    public PoitiersParkingProvider(
            RestClient.Builder restClientBuilder,
            PoitiersParkingMapper mapper,
            @Value("${parking.providers.poitiers.url}") String apiUrl) {

        this.restClient = restClientBuilder.build();
        this.mapper = mapper;
        this.apiUrl = apiUrl;
    }

    @Override
    public String getCity() {
        return "poitiers";
    }

    @Override
    public List<ParkingDto> getParkings() {

        PoitiersApiResponse response = restClient.get()
                .uri(apiUrl)
                .retrieve()
                .body(PoitiersApiResponse.class);

        if (response == null || response.results() == null) {
            throw new IllegalStateException(
                    "Invalid response from Poitiers API"
            );
        }

        return response.results()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

}
