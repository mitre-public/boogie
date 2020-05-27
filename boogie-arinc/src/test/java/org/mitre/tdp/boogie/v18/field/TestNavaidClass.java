package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.NavaidClass;

public class TestNavaidClass {

  @Test
  public void testRangePowerParsesBlank() {
    assertEquals(NavaidClass.RangePower.BLANK, NavaidClass.RangePower.SPEC.parse("AA BB"));
  }

  @Test
  public void testAdditionalInfoParsesBlank() {
    assertEquals(NavaidClass.AdditionalInfo.BLANK, NavaidClass.AdditionalInfo.SPEC.parse("AAA B"));
  }

  @Test
  public void testCollocationParsesBlank() {
    assertEquals(NavaidClass.Collocation.BLANK, NavaidClass.Collocation.SPEC.parse("AAAB "));
  }

  @Test
  public void testNavaidClassParsesAppropriatelyWithValidCodes() {
    assertEquals("VDTDN", new NavaidClass().parse("VDTDN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadType1() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parse("ADTDN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadType2() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parse("VATDN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadRangePower() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parse("VDADN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadAdditionalInfo() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parse("VDTQN"));
  }

  @Test
  public void testNavaidClassThrowsExceptionOnBadCollocation() {
    assertThrows(FieldSpecParseException.class, () -> new NavaidClass().parse("VDTDC"));
  }
}
