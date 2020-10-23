package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.utils.RunwayNumberExtractor;

public class TestRunwayNumberExtractor {

  @Test
  public void testExtractNumberOnly() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  public void testExtractNumberWithPrefix() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("RW32");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  public void testExtractLeft() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32L");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  public void testExtractRight() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32R");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  public void testExtractCenter() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32C");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  public void testExtractsNumberExcludingUnsupportedCharacter() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("32B");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @Test
  public void testExtractFull() {
    Optional<String> num = RunwayNumberExtractor.INSTANCE.runwayNumber("RW32C");
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }
}
