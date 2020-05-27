package org.mitre.tdp.boogie.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.FieldSpecParseException;

public class TestPreconditions {

  @Test
  public void testFalseThrowsException() {
    FieldSpec<?> spec = mock(FieldSpec.class);
    assertThrows(FieldSpecParseException.class, () -> checkSpec(spec, "HALP", false));
  }

  @Test
  public void testTrueCompletesSuccessfully() {
    FieldSpec<?> spec = mock(FieldSpec.class);
    checkSpec(spec, "HELP", true);
  }
}
