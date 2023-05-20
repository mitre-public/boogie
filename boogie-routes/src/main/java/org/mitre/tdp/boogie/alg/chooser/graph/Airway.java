package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.alg.resolve.AirwayToken;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

final class Airway implements GraphableToken {

  private final AirwayToken airway;

  Airway(AirwayToken airway) {
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
  public Linker visit(DirectToAirport airport) {
    return null;
  }

  @Override
  public Linker visit(org.mitre.tdp.boogie.alg.chooser.graph.Airway airway) {
    return null;
  }
}
