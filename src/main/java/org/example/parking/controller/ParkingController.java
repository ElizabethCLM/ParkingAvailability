package org.example.parking.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.parking.dto.ParkingDto;
import org.example.parking.service.ParkingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

/**
 * Controller for the Parking
 */
@RestController
@RequestMapping("/api/parkings")
@Tag(name = "Parking API", description = "Parking availability API")
public class ParkingController {
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @Operation(
            summary = "Get parking availability",
            description = "Returns parking availability for a given city"
    )
    @GetMapping
    public ResponseEntity<List<ParkingDto>> getParkings(
            @RequestParam String city) {

        List<ParkingDto> parkings = parkingService.getParkings(city);

        return ResponseEntity.ok(parkings);
    }



}
