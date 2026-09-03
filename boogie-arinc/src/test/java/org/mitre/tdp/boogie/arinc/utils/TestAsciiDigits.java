package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestAsciiDigits {

  @Test
  void parsesOnlyNonEmptyAsciiDigitSlices() {
    assertAll(
        () -> assertEquals(12.0, AsciiDigits.parseDoubleOrNaN("xx0012yy", 2, 6)),
        () -> assertTrue(Double.isNaN(AsciiDigits.parseDoubleOrNaN("", 0, 0))),
        () -> assertTrue(Double.isNaN(AsciiDigits.parseDoubleOrNaN("12A", 0, 3))),
        () -> assertTrue(Double.isNaN(AsciiDigits.parseDoubleOrNaN("١٢", 0, 2))),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> AsciiDigits.parseDoubleOrNaN("12", -1, 2))
    );
  }
}
