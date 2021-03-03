package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.split.Wildcard;

public final class AirportElement extends ResolvedElement<Airport> {

  /**
   * The {@link Wildcard}s from the route string associated with the airport element.
   */
  private final String wildcards;

  public AirportElement(String wildcards, Airport ref) {
    super(ElementType.AIRPORT, ref);
    this.wildcards = wildcards;
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    // when the airport is filed to direct in the flightplan ensure we model as a DF leg
    FixTerminationLeg leg = Wildcard.DIRECT.test(wildcards)
        ? FixTerminationLeg.DF(reference())
        : FixTerminationLeg.IF(reference());

    GraphableLeg splitLeg = new GraphableLeg(leg);
    return Collections.singletonList(new LinkedLegs(splitLeg, splitLeg));
  }
}
