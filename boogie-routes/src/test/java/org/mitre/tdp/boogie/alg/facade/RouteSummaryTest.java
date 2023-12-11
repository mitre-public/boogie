package org.mitre.tdp.boogie.alg.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;

import nl.jqno.equalsverifier.EqualsVerifier;

class RouteSummaryTest {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(RouteSummary.class).verify();
  }

  @Test
  void testRouteSummaryToFromBuilder() {
    RouteSummary summary = RouteSummary.builder()
        .route("ROUTE")
        .arrivalAirport("ARR")
        .arrivalRunway("RW")
        .arrivalFix("FIX")
        .departureAirport("DEP")
        .departureRunway("RW")
        .departureFix("FIX")
        .star("STAR")
        .starEntryFix("ENTRY")
        .requiredStarEquipage(RequiredNavigationEquipage.RNP)
        .sid("SID")
        .sidExitFix("EXIT")
        .requiredSidEquipage(RequiredNavigationEquipage.RNP)
        .approach("APCH")
        .approachEntryFix("ENTRY")
        .requiredApproachEquipage(RequiredNavigationEquipage.RNP)
        .build();

    assertEquals(summary, summary.toBuilder().build(), "To-From builder should return equivalent content.");
  }
}
