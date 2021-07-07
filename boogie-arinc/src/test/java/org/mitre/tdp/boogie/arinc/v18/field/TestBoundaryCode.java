package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestBoundaryCode {

  @Test
  public void testParseValidBoundaryCode() {
    assertEquals(BoundaryCode.USA, BoundaryCode.SPEC.parseValue("U"));
  }

  @Test
  public void testThrowsExceptionWhenBoundaryCodeNotFound() {
    assertThrows(FieldSpecParseException.class, () -> BoundaryCode.SPEC.parse("I"));
  }

  @Test
  public void testParsesAllValidBoundaryCodes() {
    Arrays.stream(BoundaryCode.values()).filter(code -> !code.equals(BoundaryCode.SPEC)).forEach(code -> assertEquals(code, BoundaryCode.SPEC.parseValue(code.boundaryCode())));
  }
}
