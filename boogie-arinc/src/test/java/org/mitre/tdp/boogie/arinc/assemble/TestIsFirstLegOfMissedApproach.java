package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

class TestIsFirstLegOfMissedApproach {

  @Test
  void testIsFirstLegOfMissedApproach() {
    assertAll(
        () -> assertFalse(IsFirstLegOfMissedApproach.INSTANCE.test(newArincProcedureLeg(null)), "Empty optional should not indicate missed."),
        () -> assertFalse(IsFirstLegOfMissedApproach.INSTANCE.test(newArincProcedureLeg("    ")), "Blank should not indicate missed."),
        () -> assertTrue(IsFirstLegOfMissedApproach.INSTANCE.test(newArincProcedureLeg("  M ")), "Third field M should indicate missed.")
    );
  }

  private ArincProcedureLeg newArincProcedureLeg(String description) {
    ArincProcedureLeg arincProcedureLEg = mock(ArincProcedureLeg.class);
    when(arincProcedureLEg.waypointDescription()).thenReturn(Optional.ofNullable(description));
    return arincProcedureLEg;
  }
}
