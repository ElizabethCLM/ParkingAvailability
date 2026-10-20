package provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.parking.provider.paris.ParisApiResponse;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.InputStream;

public class ParisParkingProviderTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeParisParkings() throws Exception {
        InputStream json = getClass().getResourceAsStream(
                "/parking_json/paris-parkings.json"
        );

        assertThat(json).isNotNull();

        ParisApiResponse response;

        try (json) {
            response = objectMapper.readValue(
                    json,
                    ParisApiResponse.class
            );
        }

        assertThat(response.total()).isEqualTo(125);
        assertThat(response.results()).hasSize(20);

        assertThat(response.results())
                .extracting(p -> p.name())
                .contains(
                        "FREMICOURT",
                        "INVALIDES",
                        "PYRAMIDES",
                        "NOTRE DAME ILE DE LA CITE",
                        "TERNES"
                );

        // Verify parking capacity
        assertThat(response.results())
                .filteredOn(p -> "INVALIDES".equals(p.name()))
                .singleElement()
                .satisfies(parking -> {
                    assertThat(parking.id()).isEqualTo("75107-P-060");
                    assertThat(parking.capacity()).isEqualTo(894);
                });

        // Verify nested geographic coordinates
        assertThat(response.results())
                .filteredOn(p -> "INVALIDES".equals(p.name()))
                .singleElement()
                .satisfies(parking -> {
                    assertThat(parking.geoPoint()).isNotNull();
                    assertThat(parking.geoPoint().lat())
                            .isEqualTo(48.859287581418464);
                    assertThat(parking.geoPoint().lon())
                            .isEqualTo(2.3146599819354683);
                });

        // Verify dataset update timestamp
        assertThat(response.results())
                .filteredOn(p -> "INVALIDES".equals(p.name()))
                .singleElement()
                .satisfies(parking ->
                        assertThat(parking.lastUpdated())
                                .isEqualTo("2026-06-01T02:00:00+00:00")
                );
    }
}
