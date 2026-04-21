package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.Airway;

class ArincAirwayValidatorTest {

  private static final ArincAirwayValidator VALIDATOR = ArincAirwayValidator.INSTANCE;

  @Test
  void validAirway() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidAirway()));
  }

  @Test
  void nullIdentifier() {
    Airway airway = TestGeneratedObjects.newValidAirway();
    airway.setIdentifier(null);
    assertFalse(VALIDATOR.test(airway));
  }

  @Test
  void nullAirwayRouteType() {
    Airway airway = TestGeneratedObjects.newValidAirway();
    airway.setAirwayRouteType(null);
    assertFalse(VALIDATOR.test(airway));
  }

  @Test
  void emptyLegs() {
    Airway airway = TestGeneratedObjects.newValidAirway();
    airway.getAirwayLeg().clear();
    assertFalse(VALIDATOR.test(airway));
  }
}
