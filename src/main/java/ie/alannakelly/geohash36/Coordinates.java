/*
 * (C) 2019 Alanna Kelly. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package ie.alannakelly.geohash36;

/**
 * Coordinates - a class for encapsulation of latitude and longitude. Instances are immutable.
 */
public class Coordinates {

  /**
   * Epsilon to use for floating point compare.
   */
  private static final double EPSILON = 0.00001;
  private final double latitude;
  private final double longitude;

  /**
   * Double comparison utility method. Based on information at https://floating-point-gui.de/errors/comparison/
   *
   * @param a - first double
   * @param b - second double
   * @return true if a & b are equal or almost equal.
   */
  private static boolean almostEqual(final double a, final double b) {
    final double absA = Math.abs(a);
    final double absB = Math.abs(b);
    final double diff = Math.abs(a - b);

    if (a == b) {
      return true;
    } else if (a == 0 || b == 0 || (absA + absB < Double.MIN_NORMAL)) {
      return diff < (Coordinates.EPSILON * Double.MIN_NORMAL);
    } else {
      return diff / Math.min(absA + absB, Double.MAX_VALUE) < Coordinates.EPSILON;
    }
  }

  /**
   * Creates an instance of Coordinates given a longitude and latitude.
   *
   * @param latitude  - a latitude in the range -90.0 to 90.0.
   * @param longitude - a longitude in the range -180.0 to 180.0.
   * @return An instance of @{@link Coordinates}.
   * @throws IllegalArgumentException if latitude or longitude are out of range.
   */
  public static Coordinates createCoordinates(final double latitude, final double longitude) {
    if ((latitude < -90.0 && latitude > 90.0)
        || (longitude < -180.0 && latitude > 180.0)) {
      throw new IllegalArgumentException("Invalid latitude or longitude.");
    }
    return new Coordinates(latitude, longitude);
  }

  private Coordinates(final double latitude, final double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Coordinates) {
      Coordinates coords = (Coordinates) obj;
      return almostEqual(this.getLatitude(), coords.getLatitude())
          && almostEqual(this.getLongitude(), coords.getLongitude());
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("(%.6f, %.6f)", latitude, longitude);
  }
}
