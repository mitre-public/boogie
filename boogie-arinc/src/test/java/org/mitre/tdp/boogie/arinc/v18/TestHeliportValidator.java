package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.base.Joiner;

public class TestHeliportValidator {
  static HeliportValidator validator =  new HeliportValidator();


  @Test
  void testIsCorrectSectionSubSection() {
    ArincRecord record = newArincRecord("latitude", "longitude", "heliportIdentifier", "heliportIcaoRegion");
    assertTrue(validator.test(record), "If we can't get this right, we are doomed");
  }


  private ArincRecord newArincRecord(String... fields) {
    Optional presentOptional = mock(Optional.class);
    when(presentOptional.isPresent()).thenReturn(true);

    ArincRecord record = mock(ArincRecord.class);
    when(record.optionalField(eq("sectionCode"))).thenReturn(Optional.of(SectionCode.H));
    when(record.optionalField(eq("subSectionCode"))).thenReturn(Optional.of("A"));
    when(record.optionalField(matches(Joiner.on("|").join(fields)))).thenReturn(presentOptional);
    return record;
  }
}
