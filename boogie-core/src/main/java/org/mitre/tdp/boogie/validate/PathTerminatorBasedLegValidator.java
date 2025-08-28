package org.mitre.tdp.boogie.validate;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;

/**
 * The {@link PathTerminatorBasedLegValidator} inspects the {@link PathTerminator} of a provided {@link Leg} and then delegates
 * that leg to the appropriate sub-predicate which inspects the field-level content of the leg according to the table outlined
 * in the 424 spec.
 * <br>
 * Note that the current implementation of this does not consider the previous/next leg if declared as part of a larger route
 * structure, though that could be added if necessary.
 * <br>
 * Note when considered as single legs collections of {@link PathTerminator} types may require the same set of fields to be present
 * even though the methodology for how they're flown is different.
 */
public final class PathTerminatorBasedLegValidator implements Predicate<Leg> {

  @Override
  public boolean test(Leg leg) {
    requireNonNull(leg.pathTerminator(), "PathTerminator must be present to validate required leg fields.");
    return switch (leg.pathTerminator()) {
      case IF, TF, DF -> leg.associatedFix().isPresent();
      case CF -> leg.associatedFix().isPresent()
          && leg.recommendedNavaid().isPresent()
          && leg.theta().isPresent()
          && leg.rho().isPresent()
          && leg.outboundMagneticCourse().isPresent()
          && leg.routeDistance().isPresent();
      case FA, FC, FD, FM -> leg.associatedFix().isPresent()
          && leg.recommendedNavaid().isPresent()
          && leg.theta().isPresent()
          && leg.rho().isPresent()
          && leg.outboundMagneticCourse().isPresent();
      case CA, VA -> leg.outboundMagneticCourse().isPresent()
          && leg.altitudeConstraint().hasLowerBound();
      case CR, VR -> leg.recommendedNavaid().isPresent()
          && leg.theta().isPresent()
          && leg.outboundMagneticCourse().isPresent();
      case CD, VD -> leg.recommendedNavaid().isPresent()
          && leg.outboundMagneticCourse().isPresent()
          && leg.routeDistance().isPresent();
      case CI, VI, VM -> leg.outboundMagneticCourse().isPresent();
      case RF -> leg.associatedFix().isPresent()
          && leg.turnDirection().isPresent()
          && leg.routeDistance().isPresent()
          && leg.centerFix().isPresent();
      case AF -> leg.associatedFix().isPresent()
          && leg.turnDirection().isPresent()
          && leg.recommendedNavaid().isPresent()
          && leg.theta().isPresent()
          && leg.rho().isPresent()
          && leg.outboundMagneticCourse().isPresent();
      case PI -> leg.associatedFix().isPresent()
          && leg.turnDirection().isPresent()
          && leg.recommendedNavaid().isPresent()
          && leg.theta().isPresent()
          && leg.rho().isPresent()
          && leg.outboundMagneticCourse().isPresent()
          && leg.routeDistance().isPresent();
      case HA, HF, HM -> leg.associatedFix().isPresent()
          && leg.turnDirection().isPresent()
          && leg.outboundMagneticCourse().isPresent()
          // only one or the other must be present on the record
          && (leg.holdTime().isPresent() || leg.routeDistance().isPresent());
      default ->
          throw new IllegalArgumentException("Unsupported leg type supplied to LegContentValidator: ".concat(leg.pathTerminator().name()));
    };
  }
}
