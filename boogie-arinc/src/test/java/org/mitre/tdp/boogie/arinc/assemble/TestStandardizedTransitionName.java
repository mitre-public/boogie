package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestStandardizedTransitionName {

  @Test
  void testStandardization() {
    assertAll(
        () -> assertEquals("RW26L", StandardizedTransitionName.INSTANCE.apply("RW26L"), "RW26L->RW26L"),
        () -> assertEquals("GOROK", StandardizedTransitionName.INSTANCE.apply("GOROK"), "GOROK->GOROK"),
        () -> assertEquals("ALL", StandardizedTransitionName.INSTANCE.apply(null), "Null->ALL"),
        () -> assertEquals("ALL", StandardizedTransitionName.INSTANCE.apply("    "), "Blank->ALL"),
        () -> assertEquals("ALL", StandardizedTransitionName.INSTANCE.apply("ALL"), "ALL->ALL")
    );
  }
}
