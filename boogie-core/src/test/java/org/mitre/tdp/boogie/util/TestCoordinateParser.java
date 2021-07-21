package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.util.CoordinateParser.convertToDegrees;
import static org.mitre.tdp.boogie.util.CoordinateParser.parse;
import static org.mitre.tdp.boogie.util.CoordinateParser.reformatLatCoordinate;
import static org.mitre.tdp.boogie.util.CoordinateParser.reformatLonCoordinate;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class TestCoordinateParser {

  private static final String lat = "30453010N";
  private static final String lon = "125453010E";

  @Test
  void testParse() {
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
  void testParseDecimalDegrees() {
    assertAll(
        () -> assertEquals(-76., convertToDegrees("273932.9118W"), 1.),
        () -> assertEquals(76., convertToDegrees("273932.9118E"), 1.),
        () -> assertEquals(43., convertToDegrees("155193.5075N"), 1.),
        () -> assertEquals(-43., convertToDegrees("155193.5075S"), 1.)
    );
  }

  @Test
  void testParseDegreesMinutesSeconds() {
    assertAll(
        () -> assertEquals(107., convertToDegrees("107-28-00.1E"), 1.),
        () -> assertEquals(-107., convertToDegrees("107-28-00.1W"), 1.),
        () -> assertEquals(-107., convertToDegrees("107-28-00.01W"), 1.),
        () -> assertEquals(-107., convertToDegrees("107-28-00.001W"), 1.),
        () -> assertEquals(-107., convertToDegrees("107-28-00.1W  "), 1.),
        () -> assertEquals(36., convertToDegrees("36-12-00.0N"), 1.),
        () -> assertEquals(-36., convertToDegrees("36-12-00.0S"), 1.)
    );
  }

  @Test
  void testReformatLatCoordinate() {
    assertAll(
        () -> assertEquals(Optional.empty(), reformatLatCoordinate("145454545E"), "Latitude should be empty with invalid initial degrees of 145 > 90"),
        () -> assertEquals(Optional.empty(), reformatLatCoordinate("45454545A"), "Latitude should be empty with invalid direction A"),
        () -> assertEquals(Optional.of("03-03-03.10S"), reformatLatCoordinate("03030310S")),
        () -> assertEquals(Optional.of("30-45-30.10N"), reformatLatCoordinate(lat)),
        () -> assertEquals(Optional.empty(), reformatLatCoordinate(lon), "Longitude should not be reformattable as a latitude")
    );
  }

  @Test
  void testReformatLonCoordinate() {
    assertAll(
        () -> assertEquals(Optional.empty(), reformatLonCoordinate("245454545S"), "Longitude should be empty with invalid initial degrees of 245 > 180"),
        () -> assertEquals(Optional.empty(), reformatLonCoordinate("145454545A"), "Longitude should be empty with invalid direction A"),
        () -> assertEquals(Optional.of("001-32-31.00W"), reformatLonCoordinate("001323100W")),
        () -> assertEquals(Optional.of("125-45-30.10E"), reformatLonCoordinate(lon)),
        () -> assertEquals(Optional.empty(), reformatLonCoordinate(lat), "Latitude should not be reformattable as a longitude")
    );
  }
}
