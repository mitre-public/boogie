package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class EquirectangularTest {
  static LatLong a = LatLong.of(20.0, 30.0);
  static LatLong b = LatLong.of(20.0, 30.009);
  static LatLong c = LatLong.of(20.0, 31.0);
  @Test
  void test() {
    assertEquals(a.distanceInNM(b), Equirectangular.distanceInNM(a, b), .00000001, "thats about a half mile at this latitude");
    assertEquals(a.distanceInNM(c), Equirectangular.distanceInNM(a, c), .001, "About 50 miles here still pretty close");
  }
}