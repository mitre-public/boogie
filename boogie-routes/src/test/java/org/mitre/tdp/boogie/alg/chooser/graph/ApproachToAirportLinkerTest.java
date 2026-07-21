package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.CF;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.newProcedure;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

/**
 * Guards the approach analogue of the {@link Linker#airportToSid}/{@link Linker#starToAirport}
 * down-selections: the airport must connect at the approach's final-transition exit (the
 * runway), never at whatever leg is geographically closest to the field. A feeder that passes
 * near the airport would, under a plain closest-point link, become a mid-procedure exit that
 * truncates the entire final.
 */
class ApproachToAirportLinkerTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  /**
   * The feeder fix OVRHD sits directly on the airport; the runway threshold RW04 is a few
   * miles out on the final. Closest-point would tap OVRHD (weight ~0) and skip the final;
   * the specialization must instead link from RW04.
   */
  @Test
  void linksFromRunwayExitNotFeederOverheadTheField() {
    double aptLat = 40.77;
    double aptLon = -73.87;

    // Feeder (route-type A / APPROACH transition) whose last fix passes right over the field.
    Leg feederStart = IF("GGREG", 40.90, -73.70);
    Leg overhead = TF("OVRHD", aptLat, aptLon);
    var feeder = transition("GGREG", "I04", "KLGA", TransitionType.APPROACH, ProcedureType.APPROACH,
        Arrays.asList(feederStart, overhead));

    // Common final: BENNG -> RW04, ending a few miles from the field on the approach course.
    Leg benng = IF("BENNG", 40.68, -73.95);
    Leg rw04 = CF("RW04", 40.73, -73.90);
    var common = transition("ALL", "I04", "KLGA", TransitionType.COMMON, ProcedureType.APPROACH,
        Arrays.asList(benng, rw04));

    Procedure approach = newProcedure(List.of(feeder, common));
    Airport klga = airport("KLGA", aptLat, aptLon);

    Linker linker = Linker.approachToAirport(anyApproach(approach), anyAirport(klga));
    Collection<LinkedLegs> links = linker.links();

    Set<String> sources = links.stream()
        .map(l -> l.source().associatedFix().map(Fix::fixIdentifier).orElse("NONE"))
        .collect(toSet());
    assertEquals(Set.of("RW04"), sources,
        "airport must link from the approach's final exit, not the feeder fix over the field: " + links);
  }

  private AnyAirport anyAirport(Airport airport) {
    return new AnyAirport(airport, GRAPHER.graphRepresentationOf(ResolvedToken.standardAirport(airport)));
  }

  private AnyApproach anyApproach(Procedure approach) {
    ResolvedToken token = ResolvedToken.standardApproach(approach);
    return new AnyApproach((Procedure) token.infrastructure(), GRAPHER.graphRepresentationOf(token));
  }
}
