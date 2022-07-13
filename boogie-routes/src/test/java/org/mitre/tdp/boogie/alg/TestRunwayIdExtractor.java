package org.mitre.tdp.boogie.alg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TestRunwayIdExtractor {

  @ParameterizedTest
  @ValueSource(strings = {"32", "RW32", "32L", "32R", "32C", "32B", "RW32C", "RW32T", "RW32W", "32W", "32T"})
  void testNumbers(String arg) {
    Optional<String> num = RunwayIdExtractor.runwayNumber(arg);
    assertTrue(num.isPresent());
    assertEquals("32", num.get());
  }

  @ParameterizedTest
  @ValueSource(strings = {"NORTH", "EAST", "SOUTH", "WEST"})
  void testNoNumbers(String arg) {
    assertTrue(RunwayIdExtractor.runwayNumber(arg).isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"32C", "RW32C"})
  void testC(String arg) {
    Optional<String> des = RunwayIdExtractor.parallelDesignator(arg);
    assertTrue(des.isPresent());
    assertEquals("C", des.get());
  }

  @ParameterizedTest
  @ValueSource(strings = {"32R", "RW32R"})
  void testR(String arg) {
    Optional<String> des = RunwayIdExtractor.parallelDesignator(arg);
    assertTrue(des.isPresent());
    assertEquals("R", des.get());
  }

  @ParameterizedTest
  @ValueSource(strings = {"32L", "RW32L"})
  void testL(String arg) {
    Optional<String> des = RunwayIdExtractor.parallelDesignator(arg);
    assertTrue(des.isPresent());
    assertEquals("L", des.get());
  }

  @ParameterizedTest
  @ValueSource(strings = {"32", "RW32", "32B", "32W", "32T", "NORTH", "EAST", "SOUTH", "WEST"})
  void testNone(String arg) {
    assertTrue(RunwayIdExtractor.parallelDesignator(arg).isEmpty());
  }
}
