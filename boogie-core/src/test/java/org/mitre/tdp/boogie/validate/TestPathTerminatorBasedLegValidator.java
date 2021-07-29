package org.mitre.tdp.boogie.validate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;

class TestPathTerminatorBasedLegValidator {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  @Test
  void testLegContentValidatorCoversAllPathTerminatorTypes() {
    assertTrue(Arrays.stream(PathTerminator.values()).noneMatch(pathTerminator -> {
      Leg leg = mockLeg(pathTerminator);
      return validator.test(leg);
    }), "The validator should have a method for each provided PathTerminator type - and with no fields set none should pass validation.");
  }

  private Leg mockLeg(PathTerminator pathTerminator) {
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    return leg;
  }
}
