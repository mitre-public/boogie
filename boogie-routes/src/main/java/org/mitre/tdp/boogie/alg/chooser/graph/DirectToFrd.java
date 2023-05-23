package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.alg.FixRadialDistance;

final class DirectToFrd implements GraphableToken {

  private final FixRadialDistance frd;

  DirectToFrd(FixRadialDistance frd) {
    this.frd = requireNonNull(frd);
  }
}
