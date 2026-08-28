package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class TestLegTimeFromString {

  @Test
  void parsesLegTimes() {
    assertAll(
        () -> assertEquals(Duration.ofSeconds(618), LegTimeFromString.INSTANCE.apply("103")),
        () -> assertThrows(IllegalArgumentException.class, () -> LegTimeFromString.INSTANCE.apply("10A")),
        () -> assertThrows(IllegalArgumentException.class, () -> LegTimeFromString.INSTANCE.apply(null))
    );
  }
}
