package mapper;

import org.example.parking.dto.ParkingDto;
import org.example.parking.mapper.PoitiersParkingMapper;
import org.example.parking.provider.poitiers.PoitiersParkingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class PoitiersParkingMapperTest {

    private PoitiersParkingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PoitiersParkingMapper();
    }

    // Correct mapping of all fields
    @Test
    void shouldMapPoitiersParkingToParkingDto() {
        PoitiersParkingResponse parking =
                new PoitiersParkingResponse(
                        3,
                        "THEATRE",
                        320,
                        99,
                        "46.58383455409422, 0.33779491061805567",
                        "2026-10-19T12:08:17.000Z"
                );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.id()).isEqualTo("poitiers-3");
        assertThat(result.name()).isEqualTo("THEATRE");
        assertThat(result.totalSpaces()).isEqualTo(320);
        assertThat(result.availableSpaces()).isEqualTo(99);

        assertThat(result.latitude())
                .isEqualTo(46.58383455409422);

        assertThat(result.longitude())
                .isEqualTo(0.33779491061805567);

        assertThat(result.lastUpdated())
                .isEqualTo(
                        Instant.parse("2026-10-19T12:08:17Z")
                );
    }

    // checks that Missing GPS coordinates become null
    @Test
    void shouldHandleMissingCoordinates() {
        // GARE EFFIA has no coordinates
        PoitiersParkingResponse parking =
                new PoitiersParkingResponse(
                        5,
                        "GARE EFFIA",
                        480,
                        251,
                        null,
                        "2026-10-19T12:08:16.000Z"
                );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.id()).isEqualTo("poitiers-5");
        assertThat(result.name()).isEqualTo("GARE EFFIA");
        assertThat(result.totalSpaces()).isEqualTo(480);
        assertThat(result.availableSpaces()).isEqualTo(251);
        assertThat(result.latitude()).isNull();
        assertThat(result.longitude()).isNull();
    }

    @Test
    void shouldHandleInvalidCoordinates() {
        PoitiersParkingResponse parking =
                new PoitiersParkingResponse(
                        3,
                        "THEATRE",
                        320,
                        99,
                        "invalid,coordinates",
                        "2026-10-19T12:08:17.000Z"
                );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.latitude()).isNull();
        assertThat(result.longitude()).isNull();
    }

    @Test
    void shouldHandleOutOfRangeCoordinates() {
        // Given: latitude cannot be greater than 90
        PoitiersParkingResponse parking =
                new PoitiersParkingResponse(
                        3,
                        "THEATRE",
                        320,
                        99,
                        "100.0, 0.33779491061805567",
                        "2026-10-19T12:08:17.000Z"
                );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.latitude()).isNull();
        assertThat(result.longitude()).isNull();
    }

    @Test
    void shouldHandleZeroParkingId() {
        // BLOSSAC TISON has Id = 0
        PoitiersParkingResponse parking =
                new PoitiersParkingResponse(
                        0,
                        "BLOSSAC TISON",
                        665,
                        363,
                        "46.57505317559496, 0.337126307915689",
                        "2026-10-19T12:08:15.000Z"
                );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.id()).isEqualTo("poitiers-0");
        assertThat(result.name()).isEqualTo("BLOSSAC TISON");
        assertThat(result.availableSpaces()).isEqualTo(363);
        assertThat(result.latitude()).isEqualTo(46.57505317559496);
        assertThat(result.longitude()).isEqualTo(0.337126307915689);
    }

    @Test
    void shouldHandleMissingLastUpdated() {
        PoitiersParkingResponse parking =
                new PoitiersParkingResponse(
                        3,
                        "THEATRE",
                        320,
                        99,
                        "46.58383455409422, 0.33779491061805567",
                        null
                );

        ParkingDto result = mapper.toDto(parking);

        assertThat(result.lastUpdated()).isNull();
    }

}
