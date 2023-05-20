package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.FixTerminationLeg;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

final class GraphableDirectToAirport implements GraphableToken {

  private final AirportElement airport;

  private final Collection<LinkedLegs> linkedLegs;

  GraphableDirectToAirport(AirportElement airport) {
    this.airport = requireNonNull(airport);
    this.linkedLegs = toLinkedLegsInternal();
  }

  private List<LinkedLegs> toLinkedLegsInternal() {
    FixTerminationLeg leg = FixTerminationLeg.DF(airport.airport());
    return singletonList(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
  }

  @Override
  public Collection<LinkedLegs> toLinkedLegs() {
    return linkedLegs;
  }

  @Override
  public Linker accept(LinkingVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public Linker visit(GraphableDirectToAirport airport) {
    return Linker.closestPointBetween(airport, this);
  }

  @Override
  public Linker visit(GraphableAirway airway) {
    return Linker.closestPointBetween(airway, this);
  }
}
