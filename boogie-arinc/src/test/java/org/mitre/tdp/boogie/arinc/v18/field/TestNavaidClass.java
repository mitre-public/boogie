package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestNavaidClass {

  @Test
  public void testRangePowerParsesBlank() {
    assertEquals(NavaidClass.RangePower.BLANK, NavaidClass.RangePower.SPEC.parseValue("AA BB"));
  }

  @Test
  public void testAdditionalInfoParsesBlank() {
    assertEquals(NavaidClass.AdditionalInfo.BLANK, NavaidClass.AdditionalInfo.SPEC.parseValue("AAA B"));
  }

  @Test
  public void testCollocationParsesBlank() {
    assertEquals(NavaidClass.Collocation.BLANK, NavaidClass.Collocation.SPEC.parseValue("AAAB "));
  }

  @Test
  public void testNavaidClassParsesAppropriatelyWithValidCodes() {
    assertEquals("VDTDN", new NavaidClass().parseValue("VDTDN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadType1() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parseValue("ADTDN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadType2() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parseValue("VATDN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadRangePower() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parseValue("VDADN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadAdditionalInfo() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parseValue("VDTQN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadCollocation() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parseValue("VDTDC"));
  }
}
