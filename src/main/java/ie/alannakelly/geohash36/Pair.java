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
 * @param <A>
 * @param <B>
 */
public class Pair<A,B> {
  public final A a;
  public final B b;

  public Pair(A a_, B b_) {
     a=a_;
     b=b_;
  }
}
