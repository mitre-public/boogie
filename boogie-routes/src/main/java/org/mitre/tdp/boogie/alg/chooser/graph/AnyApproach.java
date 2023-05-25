package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

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
    return glueIfNecessary(Linker.anyToApproach(airport, this), airport);
  }

  @Override
  public Linker visit(AnyAirway airway) {
    return glueIfNecessary(Linker.anyToApproach(airway, this), airway);
  }

  @Override
  public Linker visit(AnyApproach approach) {
    return glueIfNecessary(Linker.anyToApproach(approach, this), approach);
  }

  @Override
  public Linker visit(AnyFix fix) {
    return glueIfNecessary(Linker.anyToApproach(fix, this), fix);
  }

  @Override
  public Linker visit(AnyLatLong latLong) {
    return glueIfNecessary(Linker.anyToApproach(latLong, this), latLong);
  }

  @Override
  public Linker visit(AnySid sid) {
    return glueIfNecessary(Linker.sidToApproach(sid, this), sid);
  }

  @Override
  public Linker visit(AnyStar star) {
    return glueIfNecessary(Linker.starToApproach(star, this), star);
  }

  @Override
  public Linker visit(AnyFrd frd) {
    return glueIfNecessary(Linker.anyToApproach(frd, this), frd);
  }

  private Linker glueIfNecessary(Linker linker, LinkableToken token) {
    SectionGluer gluer = new SectionGluer();
    return () -> gluer.apply(linker.links(), token);
  }
}
