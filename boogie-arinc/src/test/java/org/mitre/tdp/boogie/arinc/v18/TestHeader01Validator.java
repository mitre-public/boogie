package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.arinc.v18.field.HeaderIdent.HDR;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;

import com.google.common.base.Joiner;

public class TestHeader01Validator {

  static Header01Validator VALIDATOR = new Header01Validator();

  @Test
  void testBasicsAreThere() {
    ArincRecord record = newArincRecord("headerIdent", "headerNumber");
    assertTrue(VALIDATOR.test(record));
  }

  private ArincRecord newArincRecord(String... fields) {
    Optional presentOptional = mock(Optional.class);
    when(presentOptional.isPresent()).thenReturn(true);

    ArincRecord record = mock(ArincRecord.class);
    when(record.requiredField(eq("headerIdent"))).thenReturn(HDR);
    when(record.requiredField(eq("headerNumber"))).thenReturn(1);
    when(record.optionalField(matches(Joiner.on("|").join(fields)))).thenReturn(presentOptional);
    return record;
  }
}
