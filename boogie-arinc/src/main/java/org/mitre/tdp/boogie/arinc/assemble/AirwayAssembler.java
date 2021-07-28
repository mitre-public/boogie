package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;

/**
 * Functional class for converting a collection of {@link ArincAirwayLeg}s into grouped and sequenced {@link Airway} records for
 * use in conjunction with downstream packages such as boogie-routes.
 */
public final class AirwayAssembler implements Function<Collection<ArincAirwayLeg>, Stream<Airway>> {

  private final FixDatabase fixDatabase;

  public AirwayAssembler(FixDatabase fixDatabase) {
    this.fixDatabase = requireNonNull(fixDatabase);
  }

  @Override
  public Stream<Airway> apply(Collection<ArincAirwayLeg> arincAirwayLegs) {
    return Stream.empty();
  }
}
