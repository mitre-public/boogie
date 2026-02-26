package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.Ndb;

class ArincNdbNavaidValidatorTest {
  private final ArincNdbNavaidValidator validator = ArincNdbNavaidValidator.INSTANCE;

  @Test
  void validNdbPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidNdb()));
  }

  @Test
  void nullRecordTypeFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setRecordType(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullIdentifierFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setIdentifier(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullIcaoCodeFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setIcaoCode(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullLocationFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setLocation(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullLatitudeFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.getLocation().setLatitude(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullLongitudeFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.getLocation().setLongitude(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullCycleDateFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setCycleDate(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullNdbFrequencyFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setNdbFrequency(null);
    assertFalse(validator.test(ndb));
  }

  @Test
  void nullNdbClassFails() {
    Ndb ndb = TestGeneratedObjects.newValidNdb();
    ndb.setNdbClass(null);
    assertFalse(validator.test(ndb));
  }
}
