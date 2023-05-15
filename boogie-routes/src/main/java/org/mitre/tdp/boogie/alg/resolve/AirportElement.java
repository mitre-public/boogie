package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.split.Wildcard;

public final class AirportElement implements ResolvedElement {

  private final Airport airport;
  /**
   * The {@link Wildcard}s from the route string associated with the airport element.
   */
  private final String wildcards;

  private final List<LinkedLegs> linkedLegs;

  AirportElement(Airport airport, String wildcards) {
    this.airport = requireNonNull(airport);
    this.wildcards = requireNonNull(wildcards);
    this.linkedLegs = this.toLinkedLegsInternal();
  }

  Airport airport() {
    return airport;
  }

  private List<LinkedLegs> toLinkedLegsInternal() {
    // when the airport is filed to direct in the flightplan ensure we model as a DF leg
    FixTerminationLeg leg = Wildcard.DIRECT.test(wildcards)
        ? FixTerminationLeg.DF(airport)
        : FixTerminationLeg.IF(airport);

    return singletonList(new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT));
  }

  @Override
  public String identifier() {
    return airport.airportIdentifier();
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

  /**
   * If transitioning from a SID direct to an airport (generally assumed to be the arrival airport - and this does happen) the
   * best we can do is assume the flight departs from the nearest SID fix to their destination.
   */
  @Override
  public List<LinkedLegs> visit(SidElement sidElement) {
    return ClosestPointBetween.INSTANCE.apply(sidElement, this);
  }

  /**
   * See the implementation in the dedicated {@link StarToAirportLinker}.
   */
  @Override
  public List<LinkedLegs> visit(StarElement starElement) {
    return StarToAirportLinker.INSTANCE.apply(starElement, this);
  }

  /**
   * Generally speaking the closest point of the Approach to the airport should also be the terminal fix of the final leg of the
   * approach (typically a runway end).
   */
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
