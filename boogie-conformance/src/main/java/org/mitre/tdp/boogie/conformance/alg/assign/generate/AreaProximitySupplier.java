package org.mitre.tdp.boogie.conformance.alg.assign.generate;

import java.util.Optional;
import java.util.function.Supplier;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

public final class AreaProximitySupplier implements Supplier<Optional<FlyableLeg>> {
  private final Route left;
  private final Route right;

  public AreaProximitySupplier(Route left, Route right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Optional<FlyableLeg> get() {
    return Optional.empty();
  }
}
