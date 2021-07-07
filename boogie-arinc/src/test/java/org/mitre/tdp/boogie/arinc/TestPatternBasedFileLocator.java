package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestPatternBasedFileLocator {

  @Test
  void testCycleSubstitution() {
    PatternBasedFileLocator locator = new PatternBasedFileLocator("/data/ARINC/{yy}/{cc}");

    assertAll(
        () -> assertEquals("/data/ARINC/19/01", locator.apply("1901").getPath()),
        () -> assertEquals("/data/ARINC/19/12", locator.apply("1912").getPath()),
        () -> assertEquals("/data/ARINC/21/03", locator.apply("2103").getPath())
    );
  }

  @Test
  void testDateSubstitution() {
    PatternBasedFileLocator locator = new PatternBasedFileLocator("/data/ARINC/{yyyy}/{mm}/{dd}");

    assertAll(
        () -> assertEquals("/data/ARINC/2019/01/03", locator.apply("1901").getPath()),
        () -> assertEquals("/data/ARINC/2019/11/07", locator.apply("1912").getPath()),
        () -> assertEquals("/data/ARINC/2021/03/25", locator.apply("2103").getPath())
    );
  }

  @Test
  void testSubstitutionReplacesMultipleCycleOccurrances() {
    PatternBasedFileLocator locator = new PatternBasedFileLocator("/data/{yy}{cc}/{yy}/{cc}");
    assertEquals("/data/2103/21/03", locator.apply("2103").getPath());
  }

  @Test
  void testSubstitutionReplacesMultipleDateOccurrances() {
    PatternBasedFileLocator locator = new PatternBasedFileLocator("/data/{yyyy}{mm}/{mm}/{dd}");
    assertEquals("/data/202103/03/25", locator.apply("2103").getPath());
  }
}
