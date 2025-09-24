package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.apache.commons.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class FlatEarthMathTest {
  static LatLong a = LatLong.of(20.0, 30.0);
  static LatLong b = LatLong.of(20.0, 30.009);
  static LatLong c = LatLong.of(20.0, 31.0);

  @Test
  void test() {
    assertEquals(a.distanceInNM(b), FlatEarthMath.distanceInNM(a, b), .00000001, "thats about a half mile at this latitude");
    assertEquals(a.distanceInNM(c), FlatEarthMath.distanceInNM(a, c), .001, "About 50 miles here still pretty close");
  }

  static LatLong one = LatLong.of(0.0, 0.0);
  static LatLong two = LatLong.of(1.0, 0.0);
  static LatLong three = LatLong.of(2.0, 0.0);
  static LatLong four = LatLong.of(3.0, 0.0);
  static LatLong five = LatLong.of(1.0, 1.0);
  static LatLong six = LatLong.of(45., 120.);

  @Test
  void test3dOnUnit() {
    Vector3D point = FlatEarthMath.toCartesian(one.latitude(), one.longitude());
    LatLong back = FlatEarthMath.toGeographic(point);
    double distance = FlatEarthMath.distanceInNM(one, back);
    assertEquals(0.0, distance, .00000001, "Should be really close");
  }

  @Test
  void test3dOffUnit() {
    Vector3D point = FlatEarthMath.toCartesian(four.latitude(), four.longitude());
    LatLong back = FlatEarthMath.toGeographic(point);
    double distance = FlatEarthMath.distanceInNM(four, back);
    assertEquals(0.0, distance, .00000001, "Should be really close");
  }

  @Test
  void testfar(){
    Vector3D point = FlatEarthMath.toCartesian(six.latitude(), six.longitude());
    LatLong back = FlatEarthMath.toGeographic(point);
    double distance = FlatEarthMath.distanceInNM(six, back);
    assertEquals(0.0, distance, .00000001, "Should be really close");
  }

  @Test
  void testRadialIntersection() {
    Optional<LatLong> point = FlatEarthMath.courseInterceptRadial(one, one.courseInDegrees(three), five, 270);
    double distance = point.orElseThrow(AssertionError::new).distanceTo(two).inFeet();
    assertEquals(0.0, distance, 1000, "Not mazing as we just did 2 datum conversions");
  }

  @Test
  void testOpposite() {
    Optional<LatLong> point = FlatEarthMath.courseInterceptRadial(three, three.courseInDegrees(one), five, 270);
    double distance = point.orElseThrow(AssertionError::new).distanceTo(two).inFeet();
    assertEquals(0.0, distance, 1000, "Not mazing as we just did 2 datum conversions");
  }

  @Test
  void testDoesNot() {
    Optional<LatLong> point = FlatEarthMath.courseInterceptRadial(one, 90.0, five, 270);
    assertTrue(point.isEmpty(), "due south does not intercept so no solution");
  }

  @Test
  void testDme() {
    LatLong firstIntercept = LatLong.of(0.5854, 0.0);
    LatLong calculated = FlatEarthMath.courseInterceptDme(one, 0.0, five, 65.0).orElseThrow(AssertionError::new);
    assertEquals(0.0, calculated.distanceTo(firstIntercept).inFeet(), 1000);
  }

  @Test
  void testInside() {
    LatLong firstIntercept = LatLong.of(1.4149, 0.0);
    LatLong calc = FlatEarthMath.courseInterceptDme(two, 0.0, five, 65.0).orElseThrow(AssertionError::new);
    assertEquals(0.0, firstIntercept.distanceTo(calc).inFeet(), 1000);
  }

  @Test
  void testInsideGoingDown() {
    LatLong firstIntercept = LatLong.of(0.5854, 0.0);
    LatLong calc = FlatEarthMath.courseInterceptDme(two, 180.0, five, 65.0).orElseThrow(AssertionError::new);
    assertEquals(0.0, firstIntercept.distanceTo(calc).inFeet(), 1000);
  }

  @Test
  void testNone() {
    assertTrue( FlatEarthMath.courseInterceptDme(two, 180.0, five, 20.0).isEmpty());
  }

  static LatLong farCenter = LatLong.of(52.7960, 6.4637);
  static LatLong farStart = LatLong.of(53.0635, 7.8045);
  static LatLong farEnd = LatLong.of(52.8740, 7.0394);

  @Test
  void testFarAwayFromUnitCircle() {
    LatLong intercept = LatLong.of(52.9290, 7.2588);
    LatLong calc = FlatEarthMath.courseInterceptDme(farStart, farStart.courseInDegrees(farEnd), farCenter, 30.0).orElseThrow(AssertionError::new);
    assertEquals(0.0, calc.distanceTo(intercept).inFeet(), 1000);
  }
}