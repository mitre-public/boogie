package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;

import java.util.Optional;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

public interface AltitudeTerminationScorer extends LegScorer {

  /**
   * Returns a weighted score based on the abs value of the delta between the target course specified in the leg and the course
   * of the flight.
   *
   * VA/CA legs have no internal magvar which can be used to compare the point true course to the specified magnetic heading in
   * a direct fashion - so we recommend adding some fudge factor here as variations can be up to 15 degrees in some areas.
   */
  Double offCourseWeight(Double courseDelta);

  /**
   * Returns a weighted score based on the signed distance to the target altitude in feet. The input to this method is always
   * taken to be the point altitude - the target altitude from the leg.
   */
  Double feetToTargetAltitudeWeight(Double altitudeDelta);

  @Override
  default Optional<Double> score(ConformablePoint that) {
    return that.pressureAltitude().isPresent() && that.trueCourse().isPresent() ? LegScorer.super.score(that) : Optional.empty();
  }

  @Override
  default double scoreAgainstLeg(ConformablePoint point) {
    Double targetAltitude = scorerLeg().current().altitudeConstraint().flatMap(AltitudeLimit::altitudeLimit).orElseThrow(supplier("Target Altitude"));
    Double outboundCourse = scorerLeg().current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));

    Double pointAltitude = point.pressureAltitude().orElseThrow(supplier("Pressure Altitude"));
    Double pointCourseTrue = point.trueCourse().orElseThrow(supplier("True Course"));

    return offCourseWeight(abs(angleDifference(pointCourseTrue, outboundCourse))) * feetToTargetAltitudeWeight(pointAltitude - targetAltitude);
  }
}
