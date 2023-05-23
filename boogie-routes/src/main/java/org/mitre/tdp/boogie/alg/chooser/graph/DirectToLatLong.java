package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import org.mitre.caasd.commons.LatLong;

final class DirectToLatLong implements GraphableToken {

  private final LatLong latLong;

  DirectToLatLong(LatLong latLong) {
    this.latLong = requireNonNull(latLong);
  }
}
