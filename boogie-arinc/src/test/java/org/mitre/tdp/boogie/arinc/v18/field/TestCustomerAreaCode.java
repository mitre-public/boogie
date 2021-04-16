package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public class TestCustomerAreaCode {

  @Test
  public void testParseValidCustomerAreaCode() {
    assertEquals(CustomerAreaCode.USA, CustomerAreaCode.SPEC.parseValue("USA"));
  }

  @Test
  public void testThrowsParseExceptionOnBadCode() {
    assertThrows(FieldSpecParseException.class, () -> CustomerAreaCode.SPEC.parseValue("NTX"));
  }

  @Test
  public void testParsesAllValidCustomerAreaCodes() {
    Arrays.stream(CustomerAreaCode.values()).filter(code -> !code.equals(CustomerAreaCode.SPEC)).forEach(code -> CustomerAreaCode.SPEC.parseValue(code.name()));
  }
}
