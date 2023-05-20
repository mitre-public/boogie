package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;

public final class DirectToAirportElement implements ResolvedElement {

  @Override
  public String identifier() {
    return null;
  }

  @Override
  public List<LinkedLegs> toLinkedLegs() {
    return null;
  }

  @Override
  public List<LinkedLegs> linksTo(ResolvedElementVisitor resolvedElementVisitor) {
    return null;
  }

  @Override
  public void accept(ResolvedTokenVisitor visitor) {

  }

  @Override
  public List<LinkedLegs> visit(AirportElement airportElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(AirwayElement airwayElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(FixElement fixElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(SidElement sidElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(StarElement starElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(ApproachElement approachElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(TailoredElement tailoredElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(LatLonElement latLonElement) {
    return null;
  }
}
