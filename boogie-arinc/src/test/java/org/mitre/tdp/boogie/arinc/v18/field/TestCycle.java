package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestCycle {

  @Test
  public void testParseAsStartDate() {
    assertEquals(Instant.parse("2019-01-03T00:00:00.00Z"), new Cycle().asStartDate("1901"));
  }

  @Test
  public void testParseExceptionNonNumericCycle() {
    assertThrows(FieldSpecParseException.class, () -> new Cycle().parseValue("A001"));
  }
}
