package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.PathTerm;

public class TestPathTerm {

  @Test
  public void testParseGoodPathTerminator() {
    assertEquals(PathTerm.AF, new org.mitre.tdp.boogie.v18.spec.field.PathTerm().parse("AF"));
  }

  @Test
  public void testParseExceptionOnBadPathTerminator() {
    assertThrows(FieldSpecParseException.class, () -> new org.mitre.tdp.boogie.v18.spec.field.PathTerm().parse("RA"));
  }
}
