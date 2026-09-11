package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincDouble;

class TestSuppressedDecimalFields {

  @Test
  void parsesSuppressedTenthsWithoutExtractingTheFieldFirst() {
    assertAll(
        () -> assertEquals(Optional.of(90.0), new ArcBearing().apply("0900")),
        () -> assertEquals(Optional.of(15.0), new ArcDistance().apply("0150")),
        () -> assertEquals(Optional.of(12.5), new LegLength().apply("125")),
        () -> assertEquals(Optional.of(15.0), new ArcDistance().parse("xx0150yy", 2, 6))
    );
  }

  @Test
  void parsesEachSuppressedDecimalScale() {
    assertAll(
        () -> assertEquals(Optional.of(15.7), suppressed(1).apply("157")),
        () -> assertEquals(Optional.of(1234567.8), suppressed(1).apply("12345678")),
        () -> assertEquals(Optional.of(.5), suppressed(1).apply("5")),
        () -> assertEquals(Optional.of(-.3), suppressed(1).apply("-03")),
        () -> assertEquals(Optional.of(9.12), suppressed(2).apply("912")),
        () -> assertEquals(Optional.of(-3.05), suppressed(3).apply("-3050")),
        () -> assertEquals(Optional.of(91.231), suppressed(3).parse("xx091231yy", 2, 8)),
        () -> assertEquals(
            Double.doubleToRawLongBits(-0.0),
            Double.doubleToRawLongBits(suppressed(3).apply("-000").orElseThrow()))
    );
  }

  @Test
  void rejectsInvalidSuppressedDecimals() {
    assertAll(
        () -> assertEquals(Optional.empty(), suppressed(1).apply("")),
        () -> assertEquals(Optional.empty(), suppressed(1).apply("   ")),
        () -> assertEquals(Optional.empty(), suppressed(1).apply("12A")),
        () -> assertEquals(Optional.empty(), suppressed(1).apply("+"))
    );
  }

  private static ArincDouble suppressed(int places) {
    return new ArincDouble() {
      @Override
      protected int suppressedDecimalPlaces() {
        return places;
      }

      @Override
      public int fieldLength() {
        return 8;
      }

      @Override
      public String fieldCode() {
        return "test";
      }
    };
  }
}
