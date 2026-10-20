package org.example.parking.provider.paris;

import org.example.parking.mapper.ParisParkingMapper;
import org.example.parking.provider.ParkingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.example.parking.dto.ParkingDto;

import java.util.List;

@Component
public class ParisParkingProvider implements ParkingProvider {

    private final RestClient restClient;
    private final ParisParkingMapper mapper;
    private final String apiUrl;

    public ParisParkingProvider(
            RestClient.Builder builder,
            ParisParkingMapper mapper,
            @Value("${parking.providers.paris.url}") String apiUrl) {

        this.restClient = builder.build();
        this.mapper = mapper;
        this.apiUrl = apiUrl;
    }

    @Override
    public String getCity() {
        return "paris";
    }

    @Override
    public List<ParkingDto> getParkings() {

        ParisApiResponse response = restClient.get()
                .uri(apiUrl)
                .retrieve()
                .body(ParisApiResponse.class);

        if (response == null || response.results() == null) {
            throw new IllegalStateException(
                    "Invalid response from Paris API"
            );
        }

        return response.results()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

}
