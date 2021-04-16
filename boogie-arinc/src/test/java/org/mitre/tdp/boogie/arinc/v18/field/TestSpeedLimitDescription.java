package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitDescription;

public class TestSpeedLimitDescription {

  @Test
  public void testBlankDescriptionIsAt() {
    assertEquals(SpeedLimitDescription.AT, SpeedLimitDescription.SPEC.parseValue(" "));
  }

  @Test
  public void testApersandDescriptionIsAt() {
    assertEquals(SpeedLimitDescription.AT, SpeedLimitDescription.SPEC.parseValue("@"));
  }

  @Test
  public void testPlusDescriptionIsAtOrAbove() {
    assertEquals(SpeedLimitDescription.AT_OR_ABOVE, SpeedLimitDescription.SPEC.parseValue("+"));
  }

  @Test
  public void testMinusDescriptionIsAtOrBelow() {
    assertEquals(SpeedLimitDescription.AT_OR_BELOW, SpeedLimitDescription.SPEC.parseValue("-"));
  }

  @Test
  public void testFailOnUnsupportedInput() {
    assertThrows(FieldSpecParseException.class, () -> SpeedLimitDescription.SPEC.parseValue("HI"));
  }
}
