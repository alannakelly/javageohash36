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
  //TODO: Test to verify output against original C version.
  //TODO: Neighbour Tests.

  @Test
  public void encode_successfulEncode_geoHash36String() {
    assertEquals("bdrdC26BqH", GeoHash36
      .encode(Coordinates.createCoordinates(51.504444, -0.086666), 10));
  }

  @Test
  public void decode_successfullDecode_Coordinate() {
    final String geoHash36 = "bdrdC26BqH";
    assertTrue(Coordinates.createCoordinates(51.504444, -0.086666)
      .equals(GeoHash36.decode(geoHash36)));
  }
}
