package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;

class TestPathTerminator {

  private static final PathTerm parser = new PathTerm();

  @Test
  void testParsesGoodPathTerminator() {
    assertEquals(Optional.of(PathTerminator.AF), parser.apply("AF"));
  }

  @Test
  void testParseExceptionOnBadPathTerminator() {
    assertEquals(Optional.empty(), parser.apply("RA"));
  }
}
