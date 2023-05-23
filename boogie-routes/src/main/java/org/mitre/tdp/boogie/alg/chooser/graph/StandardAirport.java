package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.FixTerminationLeg;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

final class StandardAirport implements GraphableToken {

  private final Airport airport;

  private final Collection<LinkedLegs> linkedLegs;

  StandardAirport(Airport airport) {
    this.airport = requireNonNull(airport);
    this.linkedLegs = createLinkedLegs(airport);
  }

  private Collection<LinkedLegs> createLinkedLegs(Airport airport) {
    Leg leg = FixTerminationLeg.IF(airport);
    return singletonList(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
  }

  @Override
  public Collection<LinkedLegs> linkedLegs() {
    return linkedLegs;
  }

  @Override
  public Linker accept(LinkingVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public Linker visit(StandardAirport airport) {
    return Linker.closestPointBetween(airport, this);
  }

  @Override
  public Linker visit(DirectToAirport airport) {
    return Linker.closestPointBetween(airport, this);
  }

  @Override
  public Linker visit(StandardAirway airway) {
    return Linker.closestPointBetween(airway, this);
  }

  @Override
  public Linker visit(StandardApproach approach) {
    return Linker.closestPointBetween(approach, this);
  }

  @Override
  public Linker visit(StandardFix fix) {
    return Linker.closestPointBetween(fix, this);
  }

  @Override
  public Linker visit(DirectToFix fix) {
    return Linker.closestPointBetween(fix, this);
  }

  @Override
  public Linker visit(StandardLatLong latLong) {
    return Linker.closestPointBetween(latLong, this);
  }

  @Override
  public Linker visit(DirectToLatLong latLong) {
    return Linker.closestPointBetween(latLong, this);
  }

  @Override
  public Linker visit(SidEnrouteCommon sid) {
    return Linker.closestPointBetween(sid, this);
  }

  @Override
  public Linker visit(SidRunway sid) {
    return Linker.closestPointBetween(sid, this);
  }

  @Override
  public Linker visit(StarEnrouteCommon star) {
    return null;
  }

  @Override
  public Linker visit(StarRunway star) {
    return null;
  }

  @Override
  public Linker visit(StandardFrd frd) {
    return Linker.closestPointBetween(frd, this);
  }

  @Override
  public Linker visit(DirectToFrd frd) {
    return Linker.closestPointBetween(frd, this);
  }
}
