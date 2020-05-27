package org.mitre.tdp.boogie.util;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.util.CoordinateParser.convertToDegrees;
import static org.mitre.tdp.boogie.util.CoordinateParser.parse;
import static org.mitre.tdp.boogie.util.CoordinateParser.reformatLatCoordinate;
import static org.mitre.tdp.boogie.util.CoordinateParser.reformatLonCoordinate;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

public class TestCoordinateParser {

  private static String lat = "30453010N";
  private static String lon = "125453010E";

  @Test
  public void testParse() {
    String loc = "1500S/12000W";
    LatLong res = parse(loc);

    assertEquals(res.latitude(), -15.0, 0.0001);
    assertEquals(res.longitude(), -120.0, 0.0001);

    loc = "3000N/09000E";
    res = parse(loc);

    assertEquals(res.latitude(), 30.0, 0.0001);
    assertEquals(res.longitude(), 90.0, 0.0001);

    loc = "3030N/12045E";
    res = parse(loc);

    assertEquals(res.latitude(), 30.5, 0.0001);
    assertEquals(res.longitude(), 120.75, 0.0001);
  }

  @Test
  public void testParseDecimalDegrees() {
    assertTrue(Math.abs(convertToDegrees("273932.9118W") + 76.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("273932.9118E") - 76.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("155193.5075N") - 43.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("155193.5075S") + 43.0) < 1.0);
  }

  @Test
  public void testParseDegreesMinutesSeconds() {
    assertTrue(Math.abs(convertToDegrees("107-28-00.1E") - 107.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("107-28-00.1W") + 107.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("107-28-00.01W") + 107.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("107-28-00.001W") + 107.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("107-28-00.1W  ") + 107.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("36-12-00.0N") - 36.0) < 1.0);
    assertTrue(Math.abs(convertToDegrees("36-12-00.0S") + 36.0) < 1.0);
  }

  @Test
  public void testReformatLatCoordinate() {
    assertEquals("30-45-30.10N", reformatLatCoordinate(lat));
    assertThrows(IllegalArgumentException.class, () -> reformatLatCoordinate(lon));
  }

  @Test
  public void testReformatLonCoordinate() {
    assertEquals("125-45-30.10E", reformatLonCoordinate(lon));
    assertThrows(IllegalArgumentException.class, () -> reformatLonCoordinate(lat));
  }
}
