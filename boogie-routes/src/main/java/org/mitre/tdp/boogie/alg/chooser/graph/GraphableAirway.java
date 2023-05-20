package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.alg.resolve.AirwayElement;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

final class GraphableAirway implements GraphableToken {

  private final AirwayElement airway;

  GraphableAirway(AirwayElement airway) {
    this.airway = requireNonNull(airway);
  }

  @Override
  public Collection<LinkedLegs> toLinkedLegs() {
    return null;
  }

  @Override
  public Linker accept(LinkingVisitor visitor) {
    return null;
  }

  @Override
  public Linker visit(GraphableDirectToAirport airport) {
    return null;
  }

  @Override
  public Linker visit(GraphableAirway airway) {
    return null;
  }
}
