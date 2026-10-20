package org.example.parking.helper;

public class CoordinateValidator {
    private CoordinateValidator() {
        // Utility class
    }

    public static boolean isValid(Double latitude, Double longitude) {
        return latitude != null
                && longitude != null
                && Double.isFinite(latitude)
                && Double.isFinite(longitude)
                && latitude >= -90
                && latitude <= 90
                && longitude >= -180
                && longitude <= 180;
    }
}
