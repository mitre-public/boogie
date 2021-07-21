package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestPathTerm {

  private static final PathTerm parser = new PathTerm();

  @Test
  void testParsesGoodPathTerminator() {
    assertEquals(Optional.of(org.mitre.tdp.boogie.PathTerm.AF), parser.apply("AF"));
  }

  @Test
  void testParseExceptionOnBadPathTerminator() {
    assertEquals(Optional.empty(), parser.apply("RA"));
  }
}
