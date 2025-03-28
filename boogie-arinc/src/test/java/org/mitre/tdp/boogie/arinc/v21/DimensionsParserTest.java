package org.mitre.tdp.boogie.arinc.v21;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

class DimensionsParserTest {
  static DimensionsParser parser = DimensionsParser.INSTANCE;

  static String REC = "00060120";
  static String CIR = "00080000";
  static String RWY = "12500120";

  @Test
  void parse() {
    Dimensions rectangles = parser.apply(REC, PadShape.S).orElseThrow();
    Dimensions circles = parser.apply(CIR, PadShape.C).orElseThrow();
    Dimensions rwy = parser.apply(RWY, PadShape.R).orElseThrow();
    Optional<Dimensions> unk = parser.apply("whatever", PadShape.U);
    assertAll(
        () -> assertEquals(120, rectangles.x()),
        () -> assertEquals(60, rectangles.y()),
        () -> assertTrue(rectangles.diameterPossible().isEmpty()),
        () -> assertEquals(80, circles.diameter()),
        () -> assertTrue(circles.xPossible().isEmpty()),
        () -> assertTrue(circles.yPossible().isEmpty()),
        () -> assertEquals(120, rwy.x()),
        () -> assertEquals(12500, rwy.y()),
        () -> assertTrue(rwy.diameterPossible().isEmpty()),
        () -> assertTrue(unk.isEmpty()),
        () -> assertThrows(IllegalStateException.class, () -> parser.apply("wahtever", PadShape.SPEC))
    );
  }
}