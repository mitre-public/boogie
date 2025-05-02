package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

/**
 * Collection of magnetic declination regression tests for the {@link Declinations} class from comparisons with published navaid
 * information and a few NOAA online calculators.
 */
class TestDeclinations {

  @Test
  void testRIC_DME() {
    double declination = Declinations.declination(-33.607394444444445, 150.79889444444444, 200., Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(12.0, declination, 1.0); // published ARINC424 JEPPESEN Cycle 1801
  }

  @Test
  void testZAPPA_WPT() {
    double declination = Declinations.declination(26.63000277777778, -79.09792222222222, 0., Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(-7.45, declination, 1.0); // noaa calculated
  }

  @Test
  void test003AT_WPT() {
    double declination = Declinations.declination(-24.726902777777777, -58.32074444444445, 0., Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(-13.72, declination, 1.0); // noaa calculated
  }

  @Test
  void testABH_DME() {
    double declination = Declinations.declination(18.24191666666667, 42.65694444444444, 6900., Instant.parse("2018-01-01T00:00:00.0Z"));
    assertEquals(2.0, declination, 1.0); // published ARINC424 JEPPESEN Cycle 1801
  }
}
