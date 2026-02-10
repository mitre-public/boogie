package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestIsFlyOverFix {

  static String yes = "EYS ";
  static String b = "NB H";
  static String no = "VE B";

  @Test
  void test() {
    assertAll(
        () -> assertTrue(IsFlyOverFix.INSTANCE.isFlyOverFix(yes)),
        () -> assertTrue(IsFlyOverFix.INSTANCE.isFlyOverFix(b)),
        () -> assertFalse(IsFlyOverFix.INSTANCE.isFlyOverFix(no))
    );
  }
}