package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Gls;

class ArincGnssLandingSystemValidatorTest {

  private static final ArincGnssLandingSystemValidator VALIDATOR = ArincGnssLandingSystemValidator.INSTANCE;

  @Test
  void validGls() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidGls()));
  }

  @Test
  void nullRecordType() {
    Gls gls = TestGeneratedObjects.newValidGls();
    gls.setRecordType(null);
    assertFalse(VALIDATOR.test(gls));
  }

  @Test
  void nullCycleDate() {
    Gls gls = TestGeneratedObjects.newValidGls();
    gls.setCycleDate(null);
    assertFalse(VALIDATOR.test(gls));
  }

  @Test
  void nullIdentifier() {
    Gls gls = TestGeneratedObjects.newValidGls();
    gls.setIdentifier(null);
    assertFalse(VALIDATOR.test(gls));
  }

  @Test
  void nullLocation() {
    Gls gls = TestGeneratedObjects.newValidGls();
    gls.setLocation(null);
    assertFalse(VALIDATOR.test(gls));
  }

  @Test
  void nullIcaoCode() {
    Gls gls = TestGeneratedObjects.newValidGls();
    gls.setIcaoCode(null);
    assertFalse(VALIDATOR.test(gls));
  }
}
