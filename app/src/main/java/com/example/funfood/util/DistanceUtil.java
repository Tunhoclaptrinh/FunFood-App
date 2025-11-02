package com.example.funfood.util;

import android.location.Location;

public class DistanceUtil {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double EARTH_RADIUS_M = 6371000.0;

    /**
     * Calculate distance between two GPS coordinates using Haversine formula
     * @return distance in kilometers
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Calculate distance in meters
     */
    public static double calculateDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        return calculateDistance(lat1, lon1, lat2, lon2) * 1000;
    }

    /**
     * Calculate distance using Android Location class
     */
    public static float calculateDistanceWithLocation(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] / 1000; // Convert to km
    }

    /**
     * Format distance for display
     * @param distanceKm distance in kilometers
     * @return formatted string (e.g., "2.5 km" or "350 m")
     */
    public static String formatDistance(double distanceKm) {
        if (distanceKm < 1) {
            int meters = (int) (distanceKm * 1000);
            return meters + " m";
        } else if (distanceKm < 10) {
            return String.format("%.1f km", distanceKm);
        } else {
            return String.format("%.0f km", distanceKm);
        }
    }

    /**
     * Calculate delivery fee based on distance
     * Same formula as backend
     */
    public static int calculateDeliveryFee(double distanceKm) {
        final int BASE_FEE = 15000;
        final int PER_KM_2_5 = 5000;
        final int PER_KM_OVER_5 = 7000;

        if (distanceKm <= 2) {
            return BASE_FEE;
        } else if (distanceKm <= 5) {
            return BASE_FEE + (int) Math.ceil(distanceKm - 2) * PER_KM_2_5;
        } else {
            return BASE_FEE + 3 * PER_KM_2_5 +
                    (int) Math.ceil(distanceKm - 5) * PER_KM_OVER_5;
        }
    }

    /**
     * Check if location is within radius
     */
    public static boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance <= radiusKm;
    }

    /**
     * Estimate delivery time based on distance
     * @param distanceKm distance in km
     * @return estimated time in minutes
     */
    public static int estimateDeliveryTime(double distanceKm) {
        // Base time: 15 minutes
        // Additional: 5 minutes per km
        int baseTime = 15;
        int additionalTime = (int) (distanceKm * 5);
        return baseTime + additionalTime;
    }

    /**
     * Format delivery time range
     */
    public static String formatDeliveryTimeRange(double distanceKm) {
        int estimatedTime = estimateDeliveryTime(distanceKm);
        int minTime = estimatedTime - 5;
        int maxTime = estimatedTime + 5;
        return minTime + "-" + maxTime + " phút";
    }
}