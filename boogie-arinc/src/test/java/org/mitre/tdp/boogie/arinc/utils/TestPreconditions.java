package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

class TestPreconditions {

  @Test
  void testFalseThrowsException() {
    FieldSpec<?> spec = mock(FieldSpec.class);
    assertThrows(FieldSpecParseException.class, () -> checkSpec(spec, "HALP", false));
  }

  @Test
  void testTrueCompletesSuccessfully() {
    FieldSpec<?> spec = mock(FieldSpec.class);
    checkSpec(spec, "HELP", true);
  }
}
