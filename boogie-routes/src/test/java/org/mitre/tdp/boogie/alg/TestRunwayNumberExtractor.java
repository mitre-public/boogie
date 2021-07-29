package org.mitre.tdp.boogie.alg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRunwayNumberExtractor {

  @Test
  void testExtractNumberOnly() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  void testExtractNumberWithPrefix() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("RW32");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  void testExtractLeft() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32L");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  void testExtractRight() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32R");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  void testExtractCenter() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32C");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  void testExtractsNumberExcludingUnsupportedCharacter() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32B");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  void testExtractFull() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("RW32C");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }
}
