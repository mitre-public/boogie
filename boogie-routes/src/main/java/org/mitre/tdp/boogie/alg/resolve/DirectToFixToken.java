package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.mitre.tdp.boogie.Fix;

public final class DirectToFixToken implements ResolvedToken {

  private final Fix fix;

  private final List<LinkedLegs> linkedLegs;

  public DirectToFixToken(Fix fix) {
    this.fix = requireNonNull(fix);
    this.linkedLegs = toLinkedLegsInternal();
  }

  private List<LinkedLegs> toLinkedLegsInternal() {
    FixTerminationLeg leg = FixTerminationLeg.DF(fix);
    return singletonList(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
  }

  @Override
  public String identifier() {
    return fix.fixIdentifier();
  }

  @Override
  public List<LinkedLegs> toLinkedLegs() {
    return linkedLegs;
  }

  @Override
  public List<LinkedLegs> linksTo(ResolvedElementVisitor resolvedElementVisitor) {
    return resolvedElementVisitor.visit(this);
  }

  @Override
  public void accept(ResolvedTokenVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public List<LinkedLegs> visit(AirportToken airportElement) {
    return ClosestPointBetween.INSTANCE.apply(airportElement, this);
  }

  @Override
  public List<LinkedLegs> visit(AirwayToken airwayElement) {
    return ClosestPointBetween.INSTANCE.apply(airwayElement, this);
  }

  @Override
  public List<LinkedLegs> visit(FixToken fixElement) {
    return ClosestPointBetween.INSTANCE.apply(fixElement, this);
  }

  @Override
  public List<LinkedLegs> visit(SidToken sidElement) {
    return ClosestPointBetween.INSTANCE.apply(sidElement, this);
  }

  @Override
  public List<LinkedLegs> visit(StarToken starElement) {
    return ClosestPointBetween.INSTANCE.apply(starElement, this);
  }

  @Override
  public List<LinkedLegs> visit(ApproachToken approachElement) {
    return ClosestPointBetween.INSTANCE.apply(approachElement, this);
  }

  @Override
  public List<LinkedLegs> visit(TailoredToken tailoredElement) {
    return ClosestPointBetween.INSTANCE.apply(tailoredElement, this);
  }

  @Override
  public List<LinkedLegs> visit(LatLonToken latLonElement) {
    return ClosestPointBetween.INSTANCE.apply(latLonElement, this);
  }
}
