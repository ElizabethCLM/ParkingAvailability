package provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.parking.provider.poitiers.PoitiersApiResponse;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PoitiersParkingProviderTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeAllNineParkings() throws Exception {
        InputStream json = getClass().getResourceAsStream(
                "/parking_json/poitiers-parkings.json"
                );

        assertThat(json).isNotNull();

        PoitiersApiResponse response;

        try (json) {
            response = objectMapper.readValue(
                    json,
                    PoitiersApiResponse.class
            );
        }

        assertThat(response.total()).isEqualTo(9);
        assertThat(response.results()).hasSize(9);

        assertThat(response.results())
                .extracting(p -> p.name())
                .contains(
                        "THEATRE",
                        "NOTRE DAME",
                        "GARE EFFIA",
                        "CORDELIERS",
                        "BLOSSAC TISON"
                );

        assertThat(response.results())
                .filteredOn(p -> p.geoPoint() == null)
                .hasSize(2);
    }
}
