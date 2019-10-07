/*
 * (C) 2019 Alanna Kelly. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package ie.alannakelly.geohash36;

/**
 * Coordinates - a class for encapsulation of latitude and longitude. Instances
 * are immutable.
 */
public class Coordinates {

    /**
     * Epsilon to use for floating point compare.
     */
    private static final double EPSILON = 0.000001;

    private final double latitude;
    private final double longitude;

    /**
     * Double comparison utility method.
     *
     * @param a
     * @param b
     * @param epsilon
     * @return
     */
    private static boolean almostEqual(final double a, final double b, final double epsilon) {
        final double absA = Math.abs(a);
        final double absB = Math.abs(b);
        final double diff = Math.abs(a - b);

        if(a==b) {
            return true;
        } else if(a == 0 || b == 0 || (absA + absB < Double.MIN_NORMAL)) {
            return diff < (epsilon * Double.MIN_NORMAL);
        } else {
            return diff / Math.min(absA + absB, Double.MAX_VALUE) < epsilon;
        }
    }

    /**
     * Creates an instance of Coordinates given a longitude and latitude.
     *
     * @param latitude
     * @param longitude
     * @return An instance of Coordinates.
     */
    public static Coordinates createCoordinates(final double latitude, final double longitude) {
        //TODO: Add range checking.
        return new Coordinates(latitude, longitude);
    }

    private Coordinates(final double latitude_, final double longitude_ ) {
        latitude = latitude_;
        longitude = longitude_;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coordinates) {
            Coordinates coord = (Coordinates)obj;
            if( almostEqual(this.getLatitude(), coord.getLatitude(), EPSILON) &&
              almostEqual(this.getLongitude(), coord.getLongitude(), EPSILON)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f)", latitude, longitude);
    }
}
