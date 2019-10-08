/*
 * (C) 2019 Alanna Kelly. All rights reserved.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package ie.alannakelly.geohash36;

/**
 * Simple immutable Pair Class. Used to return two values from a method.
 *
 * @param <A> - Type of aa.
 * @param <B> - Type of bb.
 */
public class Pair<A, B> {

  public final A aa;
  public final B bb;

  Pair(A aa, B bb) {
    this.aa = aa;
    this.bb = bb;
  }
}
