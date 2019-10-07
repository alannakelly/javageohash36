/*
 * (C) 2019 Alanna Kelly. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package ie.alannakelly.geohash36;

public enum NeighborsDir{
	NORTHWEST(0x0505),
    NORTH(0x0500),
    NORTHEAST(0x0501),
    WEST(0x0005),
    CENTER(0x0000),
    EAST(0x0001),
    SOUTHWEST(0x0105),
    SOUTH(0x0100),
    SOUTHEAST(0x0101);

    private final int value;

    NeighborsDir(final int value_){
        value = value_;
    }

    public int getValue(){
        return value;
    }
}