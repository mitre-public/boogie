package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseDouble;
import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseEastWestDouble;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestFieldSliceParser {

  @Test
  void parsesSuppressedDecimals() {
    assertAll(
        () -> assertEquals(Optional.of(15.7), parseDouble("157", 1)),
        () -> assertEquals(Optional.of(1234567.8), parseDouble("12345678", 1)),
        () -> assertEquals(Optional.of(.5), parseDouble("5", 1)),
        () -> assertEquals(Optional.of(-.3), parseDouble("-03", 1)),
        () -> assertEquals(Optional.of(9.12), parseDouble("912", 2)),
        () -> assertEquals(Optional.of(-3.05), parseDouble("-3050", 3)),
        () -> assertEquals(Optional.of(91.231), parseDouble("xx091231yy", 2, 8, 3)),
        () -> assertEquals(Optional.of(-0.0), parseDouble("-000", 3))
    );
  }

  @Test
  void rejectsInvalidDecimals() {
    assertAll(
        () -> assertEquals(Optional.empty(), parseDouble("", 1)),
        () -> assertEquals(Optional.empty(), parseDouble("   ", 1)),
        () -> assertEquals(Optional.empty(), parseDouble("12A", 1)),
        () -> assertEquals(Optional.empty(), parseDouble("+", 1))
    );
  }

  @Test
  void parsesEastWestDecimalsFromSlices() {
    assertAll(
        () -> assertEquals(Optional.of(14.0), parseEastWestDouble("xxE0140yy", 2, 7, 1)),
        () -> assertEquals(Optional.of(-14.0), parseEastWestDouble("xxW0140yy", 2, 7, 1)),
        () -> assertEquals(Optional.empty(), parseEastWestDouble("xxT0140yy", 2, 7, 1)),
        () -> assertEquals(Optional.empty(), parseEastWestDouble("W140", 0, 4, 1, 5))
    );
  }
}
