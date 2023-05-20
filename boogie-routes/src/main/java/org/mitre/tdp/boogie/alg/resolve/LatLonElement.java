package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenVisitor;
import org.mitre.tdp.boogie.alg.split.Wildcard;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.util.CoordinateParser;
import org.mitre.tdp.boogie.util.Declinations;

public final class LatLonElement implements ResolvedElement {

  private final Fix fix;

  private final String wildcards;

  private final List<LinkedLegs> linkedLegs;

  private LatLonElement(Fix fix, String wildcards) {
    this.fix = requireNonNull(fix);
    this.wildcards = wildcards;
    this.linkedLegs = toLinkedLegsInternal();
  }

  /**
   * Generates a new LatLonElement from the given string location.
   */
  public static LatLonElement from(RouteToken sectionSplit) {

    LatLong latLong = CoordinateParser.parse(CoordinateFormatStandard.makeLat(sectionSplit.infrastructureName()).orElse(""),
        CoordinateFormatStandard.makeLon(sectionSplit.infrastructureName()).orElse(""));

    double declinationApprox = Declinations.declination(
        latLong.latitude(),
        latLong.longitude(),
        null,
        Instant.now()
    );

    BoogieFix fix = new BoogieFix.Builder()
        .fixIdentifier(sectionSplit.infrastructureName())
        .fixRegion("ANONYMOUS")
        .latitude(latLong.latitude())
        .longitude(latLong.longitude())
        .modeledVariation(declinationApprox)
        .build();

    return new LatLonElement(fix, RouteTokenVisitor.wildcards(sectionSplit));
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
