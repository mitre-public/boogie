package org.mitre.tdp.boogie.util;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Collection of magnetic declination regression tests for the {@link Declinations}
 * class from comparisons with published navaid information and a few NOAA online
 * calculators.
 */
public class TestDeclinations {

  @Test
  public void testRIC_DME() {
    double declination = Declinations.declination(-33.607394444444445, 150.79889444444444, Optional.of(200.0), Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(declination, 12.0, 1.0); // published ARINC424 JEPPESEN Cycle 1801
  }

  @Test
  public void testZAPPA_WPT() {
    double declination = Declinations.declination(26.63000277777778, -79.09792222222222, Optional.of(0.0), Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(declination, -7.45, 1.0); // noaa calculated
  }

  @Test
  public void test003AT_WPT() {
    double declination = Declinations.declination(-24.726902777777777, -58.32074444444445, Optional.of(0.0), Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(declination, -13.72, 1.0); // noaa calculated
  }

  @Test
  public void testABH_DME() {
    double declination = Declinations.declination(18.24191666666667, 42.65694444444444, Optional.of(6900.0), Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(declination, 2.0, 1.0); // published ARINC424 JEPPESEN Cycle 1801
  }
}
