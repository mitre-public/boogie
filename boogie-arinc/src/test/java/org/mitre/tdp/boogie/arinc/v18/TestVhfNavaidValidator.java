package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestVhfNavaidValidator {

  private static final VhfNavaidValidator validator = new VhfNavaidValidator();

  @Test
  void testCheckIdentifier() {
    ArincRecord recordWithIdentifier = newArincRecord("vhfIdentifier");
    ArincRecord recordWithDmeIdentifier = newArincRecord("dmeIdentifier");
    ArincRecord recordWithNoIdentifier = newArincRecord();

    assertAll(
        () -> assertTrue(validator.checkIdentifier(recordWithIdentifier), "Record with standard VHF identifier should pass."),
        () -> assertTrue(validator.checkIdentifier(recordWithDmeIdentifier), "Record with DME identifier should pass."),
        () -> assertFalse(validator.checkIdentifier(recordWithNoIdentifier), "Record with no identifier should not pass.")
    );
  }

  /**
   * VHF navaids have special logic around the population of their lat/lon values. The lat/lon of the facility can come in as
   * normal latitude/longitude fields in the spec or those can be null and the ll may come in via the dmeLatitude/dmeLongitude.
   */
  @Test
  void testCheckLatLon() {
    ArincRecord recordWithLatLon = newArincRecord("latitude", "longitude");
    ArincRecord recordWithDmeLatLon = newArincRecord("dmeLatitude", "dmeLongitude");
    ArincRecord recordWithMixedLatLon = newArincRecord("latitude", "dmeLongitude");
    ArincRecord recordWithNoLatLon = newArincRecord();

    assertAll(
        () -> assertTrue(validator.checkLatLon(recordWithLatLon), "Record with standard lat/lon should pass."),
        () -> assertTrue(validator.checkLatLon(recordWithDmeLatLon), "Record with dme lat/lon should pass."),
        () -> assertFalse(validator.checkLatLon(recordWithMixedLatLon), "Record with mixed lat/lon should not pass."),
        () -> assertFalse(validator.checkLatLon(recordWithNoLatLon), "Record with no lat/lon should not pass.")
    );
  }

  private ArincRecord newArincRecord(String... fields) {
    Optional presentOptional = mock(Optional.class);
    when(presentOptional.isPresent()).thenReturn(true);

    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.D));

    if (fields.length != 0) {
      when(record.optionalField(matches(Joiner.on("|").join(fields)))).thenReturn(presentOptional);
    }
    return record;
  }
}
