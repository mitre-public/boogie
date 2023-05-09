package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.alg.resolve.ApproachResolver.PreferredProcedures.equipagePreference;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;

class TestPreferredProcedure {

  @Test
  void testPrefersRnpWhenSpecified() {
    ApproachResolver.PreferredProcedures preferHighToLow = equipagePreference(
        RequiredNavigationEquipage.RNP,
        RequiredNavigationEquipage.RNAV,
        RequiredNavigationEquipage.CONV
    );

    ApproachResolver.PreferredProcedures preferLowToHigh = equipagePreference(
        RequiredNavigationEquipage.CONV,
        RequiredNavigationEquipage.RNAV,
        RequiredNavigationEquipage.RNP
    );

    Procedure rnp = newProcedure(RequiredNavigationEquipage.RNP);
    Procedure rnav = newProcedure(RequiredNavigationEquipage.RNAV);
    Procedure conv = newProcedure(RequiredNavigationEquipage.CONV);

    assertAll(
        () -> assertEquals(singletonList(rnp), preferHighToLow.apply(Arrays.asList(conv, rnav, rnp)), "Should choose RNP"),
        () -> assertEquals(singletonList(rnav), preferHighToLow.apply(Arrays.asList(conv, rnav)), "Should choose RNAV"),
        () -> assertEquals(singletonList(conv), preferHighToLow.apply(Collections.singletonList(conv)), "Should choose CONV"),

        () -> assertEquals(singletonList(conv), preferLowToHigh.apply(Arrays.asList(rnp, rnav, conv)), "Should choose CONV"),
        () -> assertEquals(singletonList(rnav), preferLowToHigh.apply(Arrays.asList(rnav, rnp)), "Should choose RNAV"),
        () -> assertEquals(singletonList(rnp), preferLowToHigh.apply(Collections.singletonList(rnp)), "Should choose RNP")
    );
  }

  private Procedure newProcedure(RequiredNavigationEquipage equipage) {
    Procedure procedure = mock(Procedure.class);
    when(procedure.requiredNavigationEquipage()).thenReturn(equipage);
    return procedure;
  }
}
