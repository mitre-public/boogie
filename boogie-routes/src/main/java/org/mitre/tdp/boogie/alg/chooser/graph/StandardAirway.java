package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Airway;

final class StandardAirway implements GraphableToken {

  private final Airway airway;

  StandardAirway(Airway airway) {
    this.airway = requireNonNull(airway);
  }
}
