/*
 * (C) 2019 Alanna Kelly. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package ie.alannakelly.geohash36;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeoHash36Tests {
  //TODO: Neighbour Tests.

  @Test
  void encode_successfulEncode_geoHash36String() {
    assertEquals("bdrdC26BqH",
        GeoHash36.encode(Coordinates.createCoordinates(51.504444, -0.086666), 10));
  }

  @Test
  void decode_successfullDecode_Coordinate() {
    final String geoHash36 = "bdrdC26BqH";
    final Coordinates coords = Coordinates.createCoordinates(51.504444, -0.086666);
    final Coordinates decode = GeoHash36.decode(geoHash36);
    assertEquals(coords, decode);
  }

  @Test
  void JavaOutput_matches_COutput() {
    final String[] c_output = new String[]{
        "Using 10 characters for Geohash-36, our precision is 0.165480 x 0.330961 square meters\n",
        "Geohash-36 of (51.504444, -0.086666) is: bdrdC26BqH\n",
        "The Lat/Long of bdrdC26BqH is: (51.504444, -0.086666)\n",
        "Geohash36 neighbors:\n",
        "bdrdC26BqC\tbdrdC26Bqd\tbdrdC26Bq8\n",
        "bdrdC26Bqh\tbdrdC26BqH\tbdrdC26BqD\n",
        "bdrdC26BqL\tbdrdC26BqM\tbdrdC26Bqj\n"
    };

    final double latitude = 51.504444;
    final double longitude = -0.086666;

    final Coordinates coordinates = Coordinates.createCoordinates(latitude, longitude);
    final Pair<Double, Double> precision = GeoHash36
        .getPrecisionInMeters(GeoHash36.DEFAULT_NUM_CHARACTERS);

    assertEquals(c_output[0], String
        .format("Using %d characters for Geohash-36, our precision is %f x %f square meters\n",
            GeoHash36.DEFAULT_NUM_CHARACTERS, precision.aa, precision.bb));
    String hash = GeoHash36.encode(coordinates, GeoHash36.DEFAULT_NUM_CHARACTERS);
    assertEquals(c_output[1],
        String.format("Geohash-36 of %s is: %s\n", coordinates.toString(), hash));
    Coordinates decoded = GeoHash36.decode(hash);
    assertEquals(c_output[2], String.format("The Lat/Long of %s is: %s\n", hash, decoded));
    assertEquals(c_output[3], "Geohash36 neighbors:\n");
    assertEquals(c_output[4],
        String.format("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.NORTHWEST)) + String
            .format("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.NORTH)) + String
            .format("%s\n", GeoHash36.getNeighbor(hash, NeighborsDir.NORTHEAST)));
    assertEquals(c_output[5],
        String.format("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.WEST)) + String
            .format("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.CENTER)) + String
            .format("%s\n", GeoHash36.getNeighbor(hash, NeighborsDir.EAST)));
    assertEquals(c_output[6],
        String.format("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.SOUTHWEST)) + String
            .format("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.SOUTH)) + String
            .format("%s\n", GeoHash36.getNeighbor(hash, NeighborsDir.SOUTHEAST)));
  }
}
