package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Procedure;

final class StarRunway implements GraphableToken {

  private final Procedure star;

  StarRunway(Procedure star) {
    this.star = requireNonNull(star);
  }
}
