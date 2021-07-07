package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestRecordType {

  @Test
  public void testParseGoodRecordType() {
    assertEquals(RecordType.T, RecordType.SPEC.parseValue("T"));
  }

  @Test
  public void testThrowsParseExceptionOnBadRecordType() {
    assertThrows(FieldSpecParseException.class, () -> RecordType.SPEC.parseValue("A"));
  }
}
