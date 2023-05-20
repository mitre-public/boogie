package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.split.Wildcard;

public final class FixToken implements ResolvedToken {

  private final Fix fix;

  private final String wildcards;

  private final List<LinkedLegs> linkedLegs;

  public FixToken(Fix fix, String wildcards) {
    this.fix = requireNonNull(fix);
    this.wildcards = wildcards;
    this.linkedLegs = toLinkedLegsInternal();
  }

  private List<LinkedLegs> toLinkedLegsInternal() {
    FixTerminationLeg leg = Wildcard.TAILORED.test(wildcards)
        ? FixTerminationLeg.IF(fix)
        : FixTerminationLeg.DF(fix);

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
