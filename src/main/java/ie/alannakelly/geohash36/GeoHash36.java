/*
 * (C) 2019 Alanna Kelly. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package ie.alannakelly.geohash36;

public class GeoHash36 {

  static final int DEFAULT_NUM_CHARACTERS = 10;
  private static final int GEOHASH_MATRIX_SIDE = 6;
  private static final int EARTH_RADIUS_IN_METERS = 6370000;

  private static final char[] BASE_36 =
    {
      '2', '3', '4', '5', '6', '7',
      '8', '9', 'b', 'B', 'C', 'd',
      'D', 'F', 'g', 'G', 'h', 'H',
      'j', 'J', 'K', 'l', 'L', 'M',
      'n', 'N', 'P', 'q', 'Q', 'r',
      'R', 't', 'T', 'V', 'W', 'X'
    };

  private static final byte[] BASE_36_INVERSE_HASH = new byte[]{
    (byte) 0x91, (byte) 0x15, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x93, (byte) 0x18, (byte) 0x14, (byte) 0x00,
    (byte) 0x16, (byte) 0x00, (byte) 0x97, (byte) 0x1B,
    (byte) 0x99, (byte) 0x1D, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x9A, (byte) 0x1F, (byte) 0x1C, (byte) 0x00,
    (byte) 0x1E, (byte) 0x00, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x20, (byte) 0x00, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x80, (byte) 0x21, (byte) 0x81, (byte) 0x22,
    (byte) 0x82, (byte) 0x23, (byte) 0x03, (byte) 0x00,
    (byte) 0x04, (byte) 0x00, (byte) 0x05, (byte) 0x00,
    (byte) 0x06, (byte) 0x00, (byte) 0x07, (byte) 0x00,
    (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
    (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x08, (byte) 0x00, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x0B, (byte) 0x00, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x09, (byte) 0x00, (byte) 0x8A, (byte) 0x0E,
    (byte) 0x8C, (byte) 0x10, (byte) 0xFF, (byte) 0xFF,
    (byte) 0x8D, (byte) 0x12, (byte) 0x0F, (byte) 0x00
  };

  private static int fastPow(int exp_) {
    if (exp_ == 0) {
      return 1;
    }

    int output = 1;
    while (exp_ > 0) {
      output *= GeoHash36.GEOHASH_MATRIX_SIDE;
      exp_--;
    }

    return output;
  }

  private static Pair<Integer, Integer> charToIndexes(final char c_) {
    int possible_index = c_ % 36;

    final int node_ptr = possible_index << 1;
    int value = BASE_36_INVERSE_HASH[node_ptr];
    int curr = value & 0x7f;
    final int next = value & 0x80;

    char temp = BASE_36[curr];

    if (temp == c_) {
      return new Pair<>(curr / GEOHASH_MATRIX_SIDE,
        curr % GEOHASH_MATRIX_SIDE);
    } else if (next > 0) {
      value = BASE_36_INVERSE_HASH[node_ptr + 1];
      curr = (value & 0x7f);
      temp = BASE_36[curr];

      if (temp == c_) {
        return new Pair<>(curr / GEOHASH_MATRIX_SIDE,
          curr % GEOHASH_MATRIX_SIDE);
      }
    }

    throw new IllegalArgumentException(
      String.format("charToIndexes: %s not a valid GeoHash-36 character.", c_));
  }

  /**
   * Encodes a given {@link Coordinates} to a GeoHash-36 {@link String}.
   *
   * @param coordinates_ - An instance of {@link Coordinates}.
   * @param numCharacters_ - The number of characters to encode (max 10).
   * @return @{@link String} containing a GeoHash-36 code.
   */
  public static String encode(final Coordinates coordinates_,
    int numCharacters_) {
    final StringBuilder outBuffer = new StringBuilder();
    final double[] lat = {-90.0, 90.0};
    final double[] lon = {-180.0, 180.0};

    while (numCharacters_ > 0) {
      double slice = lon[0] - lon[1];
      int latIdx = 0, longIdx = 0;
      slice = Math.abs(slice) / (double) GEOHASH_MATRIX_SIDE;

      for (int i = 0; i < GEOHASH_MATRIX_SIDE; i++) {
        final double leftBoundary = (lon[0] + (i * slice));
        final double rightBoundary = (lon[0] + ((i + 1) * slice));

        if ((coordinates_.getLongitude() > leftBoundary) && (
          coordinates_.getLongitude() <= rightBoundary)) {
          longIdx = i;
          lon[0] = leftBoundary;
          lon[1] = rightBoundary;
          break;
        }
      }

      slice = lat[0] - lat[1];
      slice = Math.abs(slice) / (double) GEOHASH_MATRIX_SIDE;

      for (int i = 0; i < GEOHASH_MATRIX_SIDE; i++) {
        final double leftBoundary = (lat[0] + (i * slice));
        final double rightBoundary = (lat[0] + ((i + 1) * slice));

        if ((coordinates_.getLatitude() > leftBoundary) && (
          coordinates_.getLatitude() <= rightBoundary)) {
          latIdx = GEOHASH_MATRIX_SIDE - 1 - i;
          lat[0] = leftBoundary;
          lat[1] = rightBoundary;
          break;
        }
      }

      outBuffer.append(BASE_36[(latIdx * GEOHASH_MATRIX_SIDE) + longIdx]);
      numCharacters_--;
    }

    return outBuffer.toString();
  }

  /**
   * Decodes a {@link String} containing a GeoHash-36.
   *
   * @param buffer_ - A string containing a GeoHash-36.
   * @return An instance of {@link Coordinates} decoded from buffer_.
   */
  public static Coordinates decode(final String buffer_) {
    //bdrdC26BqH   ~~>   (51.504444, -0.086666)

    double[] lat = {-90.0, 90.0};
    double[] lon = {-180.0, 180.0};
    double slice;

    for (int i = 0; i < buffer_.length(); i++) {
      Pair<Integer, Integer> indexes = charToIndexes(buffer_.charAt(i));
      int latLine = indexes.a;
      int longCol = indexes.b;

      if (latLine == -1) {
        return null;
      }

      latLine = GEOHASH_MATRIX_SIDE - 1 - latLine;

      slice = lon[0] - lon[1];
      slice = (Math.abs(slice) / (double) GEOHASH_MATRIX_SIDE);
      lon[1] = lon[0] + (slice * (longCol + 1));
      lon[0] = lon[0] + (slice * longCol);

      slice = lat[0] - lat[1];
      slice = (Math.abs(slice) / (double) GEOHASH_MATRIX_SIDE);
      lat[1] = lat[0] + (slice * (latLine + 1));
      lat[0] = lat[0] + (slice * latLine);
    }

    return Coordinates
      .createCoordinates((lat[1] + lat[0]) / 2, (lon[1] + lon[0]) / 2);
  }

  /**
   * Returns precision in meters of a given the number of characters in a
   * GeoHash-36.
   *
   * @param numCharacters_ - number of characters to check precision of (max 10).
   * @return A {@link Pair} containing the latitude/longitude accuracy in meters.
   */
  public static Pair<Double, Double> getPrecisionInMeters(
    final int numCharacters_) {
    final double one_grade_in_meters =
      (2 * Math.PI * EARTH_RADIUS_IN_METERS) / 360;
    final double latPrec =
      (90 / (double) fastPow(numCharacters_))
        * one_grade_in_meters;
    return new Pair<>(latPrec, latPrec * 2);
  }

  /**
   * Gets the neighbouring GeoHash-36 given a GeoHash-36 and a NeighborsDir
   *
   * @param geoHash36 - A {@link String} containing a GeoHash-36.
   * @param direction - A {@link NeighborsDir}.
   * @return A {@link String} containing the GeoHash-36 in the given direction.
   */
  public static String getNeighbor(final String geoHash36,
    final NeighborsDir direction) {

    final Pair<Integer, Integer> latLong = charToIndexes(
      geoHash36.charAt(geoHash36.length() - 1));

    final byte lat_diff = (byte) (direction.getValue() >> 8);
    final byte long_diff = (byte) direction.getValue();

    final int latLine =
      (latLong.a.byteValue() + lat_diff) % GEOHASH_MATRIX_SIDE;
    final int longCol =
      (latLong.b.byteValue() + long_diff) % GEOHASH_MATRIX_SIDE;

    return geoHash36.substring(0, geoHash36.length() - 1)
      + BASE_36[(latLine * GEOHASH_MATRIX_SIDE) + longCol];
  }
}
