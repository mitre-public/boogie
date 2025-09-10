package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Procedure;

final class AnyApproach implements LinkableToken {

  private final Procedure approach;

  private final Collection<LinkedLegs> graphRepresentation;

  AnyApproach(Procedure approach, Collection<LinkedLegs> graphRepresentation) {
    this.approach = requireNonNull(approach);
    this.graphRepresentation = graphRepresentation;
  }

  Procedure approach() {
    return approach;
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
    return Linker.anyToApproach(airport, this);
  }

  @Override
  public Linker visit(AnyAirway airway) {
    return Linker.anyToApproach(airway, this);
  }

  @Override
  public Linker visit(AnyApproach approach) {
    //vi/ci legs ending approach transitions are a thing
    return Linker.ApproachNoFixToApproach(approach, this).orElseTry(Linker.approachToApproach(approach, this));
  }

  @Override
  public Linker visit(AnyFix fix) {
    return Linker.anyToApproach(fix, this);
  }

  @Override
  public Linker visit(AnyLatLong latLong) {
    return Linker.anyToApproach(latLong, this);
  }

  @Override
  public Linker visit(AnySid sid) {
    return Linker.sidNoFixToApproach(sid, this).orElseTry(Linker.sidToApproach(sid, this));
  }

  @Override
  public Linker visit(AnyStar star) {
    return Linker.starNoFixToApproach(star, this).orElseTry(Linker.starToApproach(star, this));
  }

  @Override
  public Linker visit(AnyFrd frd) {
    return Linker.anyToApproach(frd, this);
  }
}
