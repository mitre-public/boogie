package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.util.Streams;

public final class AirwayElement implements ResolvedElement {

  private static final double PUNISHMENT = .001;

  private final Airway airway;

  private final List<LinkedLegs> linkedLegs;

  AirwayElement(Airway airway) {
    this.airway = requireNonNull(airway);
    this.linkedLegs = toLinkedLegsInternal();
  }

  private List<LinkedLegs> toLinkedLegsInternal() {
    return airway.legs().size() < 2
        ? Collections.emptyList()
        : Streams.pairwise(airway.legs())
            .flatMap(pair -> Stream.of(
                // bidirectional insertion for airways
                new LinkedLegs(pair.first(), pair.second(), LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT),
                new LinkedLegs(pair.second(), pair.first(), LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT)
            )).collect(Collectors.toList());
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
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(airwayElement, this);
  }

  @Override
  public List<LinkedLegs> visit(FixElement fixElement) {
    return ClosestPointBetween.INSTANCE.apply(fixElement, this);
  }


  @Override
  public List<LinkedLegs> visit(SidElement sidElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(sidElement, this);
  }

  @Override
  public List<LinkedLegs> visit(StarElement starElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE)
        .andThen(new UnlikelyCombinationPenalizer(PUNISHMENT))
        .apply(starElement, this);
  }

  @Override
  public List<LinkedLegs> visit(ApproachElement approachElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE)
        .andThen(new UnlikelyCombinationPenalizer(PUNISHMENT))
        .apply(approachElement, this);
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
