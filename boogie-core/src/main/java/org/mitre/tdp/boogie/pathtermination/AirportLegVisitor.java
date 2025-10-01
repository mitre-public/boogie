package org.mitre.tdp.boogie.pathtermination;

import java.util.Optional;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Leg;

public final class AirportLegVisitor implements Leg.Visitor {
  Airport airport;

  public static Optional<Airport> getAirport(Leg leg) {
    AirportLegVisitor visitor = new AirportLegVisitor();
    leg.accept(visitor);
    return Optional.ofNullable(visitor.airport);
  }

  @Override
  public void visit(Leg.Standard standard) {
    airport = standard.associatedFix()
        .flatMap(FixAsAirportVisitor::getAirport)
        .orElse(null);
  }

  @Override
  public void visit(Leg.Record<?> record) {
    airport = record.associatedFix()
        .flatMap(FixAsAirportVisitor::getAirport)
        .orElse(null);
  }
}
