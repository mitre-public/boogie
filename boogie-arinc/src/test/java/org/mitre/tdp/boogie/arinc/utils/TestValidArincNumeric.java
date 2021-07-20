package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestValidArincNumeric {

  @Test
  void testFalseWithEmptyString() {
    assertFalse(ValidArincNumeric.INSTANCE.test(""));
  }

  @Test
  void testFalseWithWhitespaceString() {
    assertFalse(ValidArincNumeric.INSTANCE.test("   "));
  }

  @Test
  void testFalseIfContainingCharacters() {
    assertFalse(ValidArincNumeric.INSTANCE.test("ABC"));
  }

  @Test
  void testTrueIfPlainNumberProvided() {
    assertTrue(ValidArincNumeric.INSTANCE.test("123"));
  }

  @Test
  void testTrueIfPlusPrecedesNumber() {
    assertTrue(ValidArincNumeric.INSTANCE.test("+123"));
  }

  @Test
  void testTrueIfMinusPrecedesNumber() {
    assertTrue(ValidArincNumeric.INSTANCE.test("-123"));
  }
}
