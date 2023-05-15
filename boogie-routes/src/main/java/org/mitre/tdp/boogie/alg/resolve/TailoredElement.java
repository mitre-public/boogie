package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.split.Wildcard;
import org.mitre.tdp.boogie.model.BoogieFix;

public final class TailoredElement implements ResolvedElement {

  /**
   * The reference fix the TailoredElement is wrt.
   */
  private final Fix fix;
  /**
   * The tailoring string - e.g. ZTX010125.
   */
  private final String tailored;
  private final String wildcards;

  private final List<LinkedLegs> linkedLegs;

  public TailoredElement(Fix fix, String tailored, String wildcards) {
    this.fix = requireNonNull(fix);
    this.tailored = requireNonNull(tailored);
    this.wildcards = wildcards;
    this.linkedLegs = toLinkedLegsInternal();
  }

  private List<LinkedLegs> toLinkedLegsInternal() {
    Pair<Double, Double> bearingDistance = bearingDistance(tailored);

    double course = convertToTrue(bearingDistance.first());
    double distance = bearingDistance.second();

    LatLong projectedLocation = fix.latLong().projectOut(course, distance);

    BoogieFix asFix = new BoogieFix.Builder()
        .fixIdentifier(tailored)
        .fixRegion(fix.fixRegion())
        .latitude(projectedLocation.latitude())
        .longitude(projectedLocation.longitude())
        .publishedVariation(fix.publishedVariation().orElse(null))
        .modeledVariation(fix.modeledVariation())
        .build();

    FixTerminationLeg leg = Wildcard.TAILORED.test(wildcards)
        ? FixTerminationLeg.IF(asFix)
        : FixTerminationLeg.DF(asFix);

    return singletonList(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
  }

  private Double convertToTrue(Double bearing) {
    double magvar = fix.publishedVariation().orElse(fix.modeledVariation());
    return bearing + magvar;
  }

  /**
   * Extracts the course and distance from the tailored waypoint definition.
   */
  static Pair<Double, Double> bearingDistance(String tailored) {
    int n = tailored.length();
    Double crs = Double.parseDouble(tailored.substring(n - 6, n - 3));
    Double dist = Double.parseDouble(tailored.substring(n - 3, n));
    return Pair.of(crs, dist);
  }

  @Override
  public String identifier() {
    return tailored;
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
  public List<LinkedLegs> visit(AirportElement airportElement) {
    return ClosestPointBetween.INSTANCE.apply(airportElement, this);
  }

  @Override
  public List<LinkedLegs> visit(AirwayElement airwayElement) {
    return ClosestPointBetween.INSTANCE.apply(airwayElement, this);
  }

  @Override
  public List<LinkedLegs> visit(FixElement fixElement) {
    return ClosestPointBetween.INSTANCE.apply(fixElement, this);
  }

  @Override
  public List<LinkedLegs> visit(SidElement sidElement) {
    return ClosestPointBetween.INSTANCE.apply(sidElement, this);
  }

  @Override
  public List<LinkedLegs> visit(StarElement starElement) {
    return ClosestPointBetween.INSTANCE.apply(starElement, this);
  }

  @Override
  public List<LinkedLegs> visit(ApproachElement approachElement) {
    return ClosestPointBetween.INSTANCE.apply(approachElement, this);
  }

  @Override
  public List<LinkedLegs> visit(TailoredElement tailoredElement) {
    return ClosestPointBetween.INSTANCE.apply(tailoredElement, this);
  }

  @Override
  public List<LinkedLegs> visit(LatLonElement latLonElement) {
    return ClosestPointBetween.INSTANCE.apply(latLonElement, this);
  }
}
