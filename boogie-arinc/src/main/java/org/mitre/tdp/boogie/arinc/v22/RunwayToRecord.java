package org.mitre.tdp.boogie.arinc.v22;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

public final class RunwayToRecord implements Function<ArincRunway, Optional<ArincRecord>> {

  @Override
  public Optional<ArincRecord> apply(ArincRunway arincRunway) {
    return Optional.empty();
  }
}
