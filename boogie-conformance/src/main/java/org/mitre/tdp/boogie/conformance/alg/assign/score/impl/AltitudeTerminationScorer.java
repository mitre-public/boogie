package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;

import java.util.Optional;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

public interface AltitudeTerminationScorer extends OnLegScorer {

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
  default Optional<Double> score(ConformablePoint that, FlyableLeg legTriple) {
    return that.pressureAltitude().isPresent() && that.trueCourse().isPresent() ? OnLegScorer.super.score(that, legTriple) : Optional.empty();
  }

  @Override
  default double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    Double targetAltitude = legTriple.current().altitudeConstraint().flatMap(AltitudeLimit::altitudeLimit).orElseThrow(supplier("Target Altitude"));
    Double outboundCourse = legTriple.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));

    Double pointAltitude = point.pressureAltitude().orElseThrow(supplier("Pressure Altitude"));
    Double pointCourseTrue = point.trueCourse().orElseThrow(supplier("True Course"));

    return offCourseWeight(abs(angleDifference(pointCourseTrue, outboundCourse))) * feetToTargetAltitudeWeight(pointAltitude - targetAltitude);
  }
}
