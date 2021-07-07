package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestAltitudeDescription {

  @Test
  public void testThrowsExceptionWhenValueNotAllowed() {
    AltitudeDescription spec = new AltitudeDescription();
    assertThrows(FieldSpecParseException.class, () -> spec.parseValue("_"));
  }

  @Test
  public void testParsesNormallyWhenValueAllowed() {
    AltitudeDescription spec = new AltitudeDescription();
    assertEquals("+", spec.parseValue("+"));
  }

  @Test
  public void testAllowsAllAllowedValues() {
    AltitudeDescription spec = new AltitudeDescription();
    spec.allowedValues().forEach(value -> assertEquals(value, spec.parseValue(value)));
  }
}
