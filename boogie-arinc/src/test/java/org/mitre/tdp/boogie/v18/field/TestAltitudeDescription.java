package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.AltitudeDescription;

public class TestAltitudeDescription {

  @Test
  public void testThrowsExceptionWhenValueNotAllowed() {
    AltitudeDescription spec = new AltitudeDescription();
    assertThrows(FieldSpecParseException.class, () -> spec.parse("_"));
  }

  @Test
  public void testParsesNormallyWhenValueAllowed() {
    AltitudeDescription spec = new AltitudeDescription();
    assertEquals("+", spec.parse("+"));
  }

  @Test
  public void testAllowsAllAllowedValues() {
    AltitudeDescription spec = new AltitudeDescription();
    spec.allowedValues().forEach(value -> assertEquals(value, spec.parse(value)));
  }
}
