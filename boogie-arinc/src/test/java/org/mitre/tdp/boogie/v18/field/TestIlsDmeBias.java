package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.IlsDmeBias;

public class TestIlsDmeBias {

  @Test
  public void testParseValidBias() {
    assertEquals(9.6d, new IlsDmeBias().parseValue("96"));
  }

  @Test
  public void testParseInvalidBias() {
    assertThrows(FieldSpecParseException.class, () -> new IlsDmeBias().parseValue("A5"));
  }
}
