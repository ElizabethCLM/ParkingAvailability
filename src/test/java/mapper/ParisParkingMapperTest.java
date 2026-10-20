package mapper;

import org.example.parking.dto.ParkingDto;
import org.example.parking.mapper.ParisParkingMapper;
import org.example.parking.provider.paris.ParisParkingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class ParisParkingMapperTest {
    private ParisParkingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ParisParkingMapper();
    }

    @Test
    void shouldMapParisParkingToParkingDto() {
        ParisParkingResponse parking = new ParisParkingResponse(
                "75107-P-060",
                "INVALIDES",
                894,
                new ParisParkingResponse.GeoPoint(48.85928758, 2.31465998),
                "2026-06-01T02:00:00+00:00"
        );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.id()).isEqualTo("paris-75107-P-060");
        assertThat(result.name()).isEqualTo("INVALIDES");
        assertThat(result.totalSpaces()).isEqualTo(894);
        assertThat(result.availableSpaces()).isNull();
        assertThat(result.latitude()).isEqualTo(48.85928758);
        assertThat(result.longitude()).isEqualTo(2.31465998);
        assertThat(result.lastUpdated())
                .isEqualTo(Instant.parse("2026-06-01T02:00:00Z"));
    }

    @Test
    void shouldHandleMissingCoordinates() {
        ParisParkingResponse parking = new ParisParkingResponse(
                "75112-P-101",
                "MEDITERRANEE",
                1409,
                null,
                "2026-06-01T02:00:00+00:00"
        );

        ParkingDto result = mapper.toDto(parking);
        assertThat(result.latitude()).isNull();
        assertThat(result.longitude()).isNull();
    }

    @Test
    void shouldHandlePartiallyMissingCoordinates() {
        ParisParkingResponse parking = new ParisParkingResponse(
                "75112-P-101",
                "MEDITERRANEE",
                1409,
                new ParisParkingResponse.GeoPoint(48.84497657, null),
                "2026-06-01T02:00:00+00:00"
        );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.latitude()).isNull();
        assertThat(result.longitude()).isNull();
    }

    @Test
    void shouldHandleMissingAvailability() {
        ParisParkingResponse parking = new ParisParkingResponse(
                "75101-P-052",
                "PYRAMIDES",
                562,
                new ParisParkingResponse.GeoPoint(48.86512039, 2.33318657),
                "2026-06-01T02:00:00+00:00"
        );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.totalSpaces()).isEqualTo(562);
        assertThat(result.availableSpaces()).isNull();
    }

    @Test
    void shouldHandleMissingTimestamp() {
        ParisParkingResponse parking = new ParisParkingResponse(
                "75107-P-060",
                "INVALIDES",
                894,
                new ParisParkingResponse.GeoPoint(48.85928758, 2.31465998),
                null
        );

        ParkingDto result = mapper.toDto(parking);
        assertThat(result.lastUpdated()).isNull();
    }

    @Test
    void shouldHandleInvalidCoordinates() {
        // Given: latitude 100.0 is outside the valid range
        ParisParkingResponse parking = new ParisParkingResponse(
                "75107-P-060",
                "INVALIDES",
                894,
                new ParisParkingResponse.GeoPoint(100.0, 2.31465998),
                "2026-06-01T02:00:00+00:00"
        );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.latitude()).isNull();
        assertThat(result.longitude()).isNull();
    }

    @Test
    void shouldHandleNonFiniteCoordinates() {
        ParisParkingResponse parking = new ParisParkingResponse(
                "75107-P-060",
                "INVALIDES",
                894,
                new ParisParkingResponse.GeoPoint(Double.NaN, 2.31465998),
                "2026-06-01T02:00:00+00:00"
        );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.latitude()).isNull();
        assertThat(result.longitude()).isNull();
    }
}