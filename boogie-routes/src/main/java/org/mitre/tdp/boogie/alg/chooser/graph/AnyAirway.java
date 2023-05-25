package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static org.mitre.caasd.commons.Distance.ofNauticalMiles;

import java.util.Collection;
import java.util.function.DoubleUnaryOperator;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

final class AnyAirway implements LinkableToken {

  private static final DoubleUnaryOperator PUNISHER = weight -> weight + .001;

  private final Airway airway;

  private final Collection<LinkedLegs> graphRepresentation;

  AnyAirway(Airway airway, Collection<LinkedLegs> graphRepresentation) {
    this.airway = requireNonNull(airway);
    this.graphRepresentation = graphRepresentation;
  }

  Airway airway() {
    return airway;
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
    return Linker.tweak(multiLinker(approach), PUNISHER);
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
    return Linker.tweak(multiLinker(star), PUNISHER);
  }

  @Override
  public Linker visit(AnyFrd frd) {
    return Linker.closestPointBetween(frd, this);
  }

  private Linker multiLinker(LinkableToken token) {
    return Linker.pointsWithinRange(ofNauticalMiles(.25), token, this).orElseTry(Linker.closestPointBetween(token, this));
  }
}
