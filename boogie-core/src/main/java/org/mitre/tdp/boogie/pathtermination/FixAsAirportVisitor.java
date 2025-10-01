package org.mitre.tdp.boogie.pathtermination;

import java.util.Optional;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;

/**
 * This class will pull out an airport from a Fix.Record if its there.
 */
public final class FixAsAirportVisitor implements Fix.Visitor{

  private Airport airport;

  public static Optional<Airport> getAirport(Fix fix) {
    FixAsAirportVisitor visitor = new FixAsAirportVisitor();
    fix.accept(visitor);
    return Optional.ofNullable(visitor.airport);
  }

  @Override
  public void visit(Fix.Standard standard) {
    airport = null;
  }

  @Override
  public void visit(Fix.Record<?> record) {
    airport = Optional.of(record)
        .map(Fix.Record::datum)
        .filter(d -> Airport.class.isAssignableFrom(d.getClass()))
        .map(Airport.class::cast)
        .orElse(null);
  }

  @Override
  public void visit(Fix.FixRadialDistance frd) {
    airport = null;
  }
}
