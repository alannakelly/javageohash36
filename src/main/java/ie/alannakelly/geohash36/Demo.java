/*
 * (C) 2019 Alanna Kelly. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package ie.alannakelly.geohash36;

/**
 * Ported directly from the C code.
 */
class Demo {

  public static void main(String[] args) {

    //(51.504444, -0.086666)  -->   bdrdC26BqH

    double latitude = 51.504444;
    double longitude = -0.086666;

    Coordinates coordinates = Coordinates.createCoordinates(latitude, longitude);
    Pair<Double, Double> precision = GeoHash36
        .getPrecisionInMeters(GeoHash36.DEFAULT_NUM_CHARACTERS);

    System.out
        .printf("Using %d characters for Geohash-36, our precision is %f x %f square meters\n",
            GeoHash36.DEFAULT_NUM_CHARACTERS, precision.aa, precision.bb);

    String hash = GeoHash36.encode(coordinates, GeoHash36.DEFAULT_NUM_CHARACTERS);

    System.out.printf("Geohash-36 of %s is: %s\n", coordinates.toString(), hash);

    Coordinates decoded = GeoHash36.decode(hash);

    System.out.printf("The Lat/Long of %s is: %s\n", hash, decoded);

    System.out.print("Geohash36 neighbors:\n");

    System.out.printf("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.NORTHWEST));
    System.out.printf("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.NORTH));
    System.out.printf("%s\n", GeoHash36.getNeighbor(hash, NeighborsDir.NORTHEAST));

    System.out.printf("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.WEST));
    System.out.printf("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.CENTER));
    System.out.printf("%s\n", GeoHash36.getNeighbor(hash, NeighborsDir.EAST));

    System.out.printf("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.SOUTHWEST));
    System.out.printf("%s\t", GeoHash36.getNeighbor(hash, NeighborsDir.SOUTH));
    System.out.printf("%s\n", GeoHash36.getNeighbor(hash, NeighborsDir.SOUTHEAST));
  }
}