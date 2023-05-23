package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import org.mitre.caasd.commons.LatLong;

final class StandardLatLong implements GraphableToken {

  private final LatLong latLong;

  StandardLatLong(LatLong latLong) {
    this.latLong = requireNonNull(latLong);
  }
}
