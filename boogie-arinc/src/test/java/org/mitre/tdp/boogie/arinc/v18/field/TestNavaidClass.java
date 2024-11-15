package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestNavaidClass {

  private static final NavaidClass parser = new NavaidClass();


  @Test
  void testParserFiltersWhitespaceInput() {
    assertEquals(Optional.of("     "), parser.apply("     "));
  }

  @Test
  void testParserReturns() {
    assertAll(
        () -> assertEquals(Optional.of("VDTDN"), parser.apply("VDTDN"), "All valid codes should return all codes."),
        () -> assertEquals(Optional.of("     "), parser.apply("AAAQC"), "All invalid codes should return blank string.")
    );
  }
}
