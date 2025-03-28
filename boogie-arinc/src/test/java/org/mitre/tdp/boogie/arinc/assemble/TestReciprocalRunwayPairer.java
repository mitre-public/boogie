package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

class TestReciprocalRunwayPairer {

  private static final ReciprocalRunwayPairer allPairer = new ReciprocalRunwayPairer((r1, r2) -> true);

  @Test
  void testThrowsExceptionWhenSharedReciprocals() {
    ArincRunway r1 = mock(ArincRunway.class);
    ArincRunway r2 = mock(ArincRunway.class);
    ArincRunway r3 = mock(ArincRunway.class);
    ArincRunway r4 = mock(ArincRunway.class);
    ArincRunway r5 = mock(ArincRunway.class);

    Set<RunwayPair> pairs = newHashSet(new RunwayPair(r1, r2), new RunwayPair(r3, r4), new RunwayPair(r1, r5));
    assertThrows(IllegalArgumentException.class, () -> allPairer.checkNoSharedReciprocals(pairs));
  }

  @Test
  void testUnpairedRunwaysReturnsCorrectUnpaired() {
    ArincRunway r1 = mock(ArincRunway.class);
    ArincRunway r2 = mock(ArincRunway.class);
    ArincRunway r3 = mock(ArincRunway.class);
    ArincRunway r4 = mock(ArincRunway.class);
    ArincRunway r5 = mock(ArincRunway.class);

    Set<ArincRunway> allRunways = newHashSet(r1, r2, r3, r4, r5);
    Set<RunwayPair> pairs = newHashSet(new RunwayPair(r1, r2), new RunwayPair(r3, r4));

    Set<ArincRunway> unpaired = allPairer.allUnpairedRunways(pairs, allRunways);

    assertAll(
        () -> assertEquals(1, unpaired.size()),
        () -> assertEquals(r5, unpaired.iterator().next())
    );
  }

  @Test
  void testAllReciprocalsAtAirportThrowsExceptionOnMismatchedAirports() {
    ArincRunway r1 = newRunway("KATL", "K2", "13R");
    ArincRunway r2 = newRunway("KDCA", "K2", "31L");

    assertThrows(IllegalArgumentException.class, () -> allPairer.apply(newHashSet(r1, r2)));
  }

  @Test
  void testAllReciprocalsReturnsAppropriatePairs() {
    ArincRunway r1 = mock(ArincRunway.class);
    ArincRunway r2 = mock(ArincRunway.class);
    ArincRunway r3 = mock(ArincRunway.class);
    ArincRunway r4 = mock(ArincRunway.class);
    ArincRunway r5 = mock(ArincRunway.class);

    ReciprocalRunwayPairer pairer = new ReciprocalRunwayPairer((ar1, ar2) -> (ar1.equals(r1) && ar2.equals(r2)) || (ar1.equals(r2) && ar2.equals(r1)));

    Collection<RunwayPair> allPairs = pairer.apply(newHashSet(r1, r2, r3, r4, r5));

    assertAll(
        () -> assertEquals(4, allPairs.size()),
        () -> assertTrue(allPairs.contains(new RunwayPair(r1, r2)) || allPairs.contains(new RunwayPair(r2, r1))),
        () -> assertTrue(allPairs.contains(new RunwayPair(r3, null)))
    );
  }

  private ArincRunway newRunway(String airport, String region, String runwayId) {
    ArincRunway runway = mock(ArincRunway.class);
    when(runway.airportIdentifier()).thenReturn(airport);
    when(runway.airportIcaoRegion()).thenReturn(region);
    when(runway.runwayIdentifier()).thenReturn(runwayId);
    when(runway.toString()).thenReturn(String.join(":", airport, region, runwayId));
    return runway;
  }
}
