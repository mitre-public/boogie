package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static org.mitre.caasd.commons.Distance.ofNauticalMiles;

import java.util.Collection;

import org.mitre.tdp.boogie.Procedure;

final class AnyStar implements LinkableToken {

  private final Procedure star;

  private final Collection<LinkedLegs> graphRepresentation;

  AnyStar(Procedure star, Collection<LinkedLegs> graphRepresentation) {
    this.star = requireNonNull(star);
    this.graphRepresentation = graphRepresentation;
  }

  Procedure star() {
    return star;
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
    return multiLinker(airway);
  }

  @Override
  public Linker visit(AnyApproach approach) {
    return multiLinker(approach); // penalize APCH -> STAR?
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
    return multiLinker(sid);
  }

  @Override
  public Linker visit(AnyStar star) {
    return multiLinker(star);
  }

  @Override
  public Linker visit(AnyFrd frd) {
    return Linker.closestPointBetween(frd, this);
  }

  private Linker multiLinker(LinkableToken token) {
    return Linker.pointsWithinRange(ofNauticalMiles(.25), token, this).orElseTry(Linker.closestPointBetween(token, this));
  }
}
