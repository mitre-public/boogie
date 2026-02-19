package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestStandardizedTransitionName {

  @Test
  void testStandardization() {
    assertAll(
        () -> assertEquals("RW26L", org.mitre.tdp.boogie.util.StandardizedTransitionName.INSTANCE.apply("RW26L"), "RW26L->RW26L"),
        () -> assertEquals("GOROK", org.mitre.tdp.boogie.util.StandardizedTransitionName.INSTANCE.apply("GOROK"), "GOROK->GOROK"),
        () -> assertEquals("ALL", org.mitre.tdp.boogie.util.StandardizedTransitionName.INSTANCE.apply(null), "Null->ALL"),
        () -> assertEquals("ALL", org.mitre.tdp.boogie.util.StandardizedTransitionName.INSTANCE.apply("    "), "Blank->ALL"),
        () -> assertEquals("ALL", org.mitre.tdp.boogie.util.StandardizedTransitionName.INSTANCE.apply("ALL"), "ALL->ALL")
    );
  }
}
