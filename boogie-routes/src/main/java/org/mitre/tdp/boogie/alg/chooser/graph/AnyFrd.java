package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.alg.FixRadialDistance;

final class AnyFrd implements LinkableToken {

  private final FixRadialDistance frd;

  private final Collection<LinkedLegs> graphRepresentation;

  AnyFrd(FixRadialDistance frd, Collection<LinkedLegs> graphRepresentation) {
    this.frd = requireNonNull(frd);
    this.graphRepresentation = graphRepresentation;
  }

  FixRadialDistance frd() {
    return frd;
  }

  @Override
  public Collection<LinkedLegs> graphRepresentation() {
    return graphRepresentation;
  }

  @Override
  public void accept(LinkableTokenVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public Linker accept(LinkingVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public Linker visit(AnyAirport airport) {
    return Linker.closestPointBetween(airport, this);
  }

  @Override
  public Linker visit(AnyAirway airway) {
    return Linker.closestPointBetween(airway, this);
  }

  @Override
  public Linker visit(AnyApproach approach) {
    return Linker.closestPointBetween(approach, this);
  }

  @Override
  public Linker visit(AnyFix fix) {
    return Linker.closestPointBetween(fix, this);
  }

  @Override
  public Linker visit(AnyLatLong latLong) {
    return Linker.closestPointBetween(latLong, this);
  }

  @Override
  public Linker visit(AnySid sid) {
    return Linker.closestPointBetween(sid, this);
  }

  @Override
  public Linker visit(AnyStar star) {
    return Linker.closestPointBetween(star, this);
  }

  @Override
  public Linker visit(AnyFrd frd) {
    return Linker.closestPointBetween(frd, this);
  }
}
