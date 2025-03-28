package org.mitre.tdp.boogie.alg.split;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.split.RouteToken.faaIfrBuilder;
import static org.mitre.tdp.boogie.alg.split.RouteToken.icaoBuilder;
import static org.mitre.tdp.boogie.alg.split.RouteToken.standard;
import static org.mitre.tdp.boogie.alg.split.RouteTokenVisitor.isDirectTo;
import static org.mitre.tdp.boogie.alg.split.RouteTokenVisitor.isTailoredBefore;

import org.junit.jupiter.api.Test;

class RouteTokenVisitorTest {

  @Test
  void testIsDirectTo() {
    assertAll(
        () -> assertFalse(isDirectTo(standard("A", 0.)), "Standard: False"),

        () -> assertFalse(isDirectTo(faaIfrBuilder("A", 0.).build()), "FAA IFR: False"),
        () -> assertTrue(isDirectTo(faaIfrBuilder("A", 0.).wildcards(" ").build()), "FAA IFR: True"),

        () -> assertFalse(isDirectTo(icaoBuilder("A", 0.).build()), "ICAO: False"),
        () -> assertTrue(isDirectTo(icaoBuilder("A", 0.).wildcards(" ").build()), "ICAO: True")
    );
  }

  @Test
  void testIsTailoredTo() {
    assertAll(
        () -> assertFalse(isTailoredBefore(standard("A", 0.)), "Standard: False"),

        () -> assertFalse(isTailoredBefore(faaIfrBuilder("A", 0.).build()), "FAA IFR: False"),
        () -> assertTrue(isTailoredBefore(faaIfrBuilder("A", 0.).wildcards("/").build()), "FAA IFR: True"),

        () -> assertFalse(isTailoredBefore(icaoBuilder("A", 0.).build()), "ICAO: False"),
        () -> assertTrue(isTailoredBefore(icaoBuilder("A", 0.).wildcards("/").build()), "ICAO: True")
    );
  }
}
