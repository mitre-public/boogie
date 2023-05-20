package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;

public final class DirectToAirportToken implements ResolvedToken {

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
  public List<LinkedLegs> visit(AirportToken airportElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(AirwayToken airwayElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(FixToken fixElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(SidToken sidElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(StarToken starElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(ApproachToken approachElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(TailoredToken tailoredElement) {
    return null;
  }

  @Override
  public List<LinkedLegs> visit(LatLonToken latLonElement) {
    return null;
  }
}
