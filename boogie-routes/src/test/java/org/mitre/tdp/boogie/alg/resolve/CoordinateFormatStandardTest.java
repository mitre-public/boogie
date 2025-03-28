package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.CoordinateFormatStandard.makeLat;
import static org.mitre.tdp.boogie.alg.resolve.CoordinateFormatStandard.makeLon;
import static org.mitre.tdp.boogie.alg.resolve.CoordinateFormatStandard.supported;

import org.junit.jupiter.api.Test;

class CoordinateFormatStandardTest {

  @Test
  void testSupported() {
    assertAll(
        () -> assertTrue(supported("5300N/14000W"), "FAA format should be supported"),
        () -> assertTrue(supported("53N140W"), "ICAO format (degrees only) should be supported"),
        () -> assertTrue(supported("5300N14000W"), "ICAO format (degrees and minutes) should be supported"),
        () -> assertFalse(supported("123466"), "Random numbers not supported")
    );
  }

  @Test
  void testFaa() {
    assertAll(
        () -> assertEquals("530000N", makeLat("5300N/14000W").orElse(null), "FAA Latitude"),
        () -> assertEquals("1400000W", makeLon("5300N/14000W").orElse(null), "FAA Longitude")
    );
  }

  @Test
  void testIcao() {
    assertAll(
        () -> assertEquals("530000N", makeLat("53N140W").orElse(null), "ICAO (Degrees Only) Latitude"),
        () -> assertEquals("1400000W", makeLon("53N140W").orElse(null), "ICAO (Degrees Only) Longitude"),

        () -> assertEquals("530000N", makeLat("5300N14000W").orElse(null), "ICAO Latitude"),
        () -> assertEquals("1400000W", makeLon("5300N14000W").orElse(null), "ICAO Longitude")
    );
  }
}
