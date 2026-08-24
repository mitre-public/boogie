package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ReferencedCourse;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.conformance.alg.assign.score.StandardLegFeatureExtractor;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;

class TestCrVrReferencedCourseConformance {

  private static final double MAGNETIC_THETA = 100.;
  private static final double TRUE_COURSE = 110.;
  private static final Fix NAVAID = Fix.builder()
      .fixIdentifier("NAV")
      .latLong(LatLong.of(45., -75.))
      .magneticVariation(MagneticVariation.ofDegrees(10.))
      .build();

  @Test
  void crThetaUsesNavaidVariationIndependentlyOfOutboundCourseReference() {
    assertDistances(PathTerminator.CR, ReferencedCourse.trueCourse(TRUE_COURSE));
    assertDistances(PathTerminator.CR, ReferencedCourse.magnetic(MAGNETIC_THETA));
  }

  @Test
  void vrThetaUsesNavaidVariationIndependentlyOfOutboundCourseReference() {
    assertDistances(PathTerminator.VR, ReferencedCourse.trueCourse(TRUE_COURSE));
    assertDistances(PathTerminator.VR, ReferencedCourse.magnetic(MAGNETIC_THETA));
  }

  @Test
  void standardExtractorDispatchesValidVrWithTrueOutboundCourse() {
    FlyableLeg flyableLeg = flyableLeg(PathTerminator.VR, ReferencedCourse.trueCourse(TRUE_COURSE));
    ConformablePoint point = pointOnTrueRadial();

    ViterbiFeatureVector vector = new StandardLegFeatureExtractor()
        .apply(point, flyableLeg)
        .apply(point, flyableLeg);

    assertTrue(vector.containsFeature(VrFeatureExtractor.LEG_TYPE));
    assertFalse(vector.containsFeature(CrFeatureExtractor.LEG_TYPE));
    assertEquals(1., vector.featureValue(VrFeatureExtractor.LEG_TYPE));
    assertEquals(0., vector.featureValue(VrFeatureExtractor.DEGREES_OFF_COURSE), 1e-9);
    assertEquals(0., vector.featureValue(VrFeatureExtractor.NEAREST_YOU_GOT), .01);
  }

  private void assertDistances(PathTerminator pathTerminator, ReferencedCourse outboundCourse) {
    FlyableLeg flyableLeg = flyableLeg(pathTerminator, outboundCourse);
    ConformablePoint point = pointOnTrueRadial();

    assertEquals(0., LegDistance.deriveDegreesOffCourse(point, flyableLeg), 1e-9);
    assertEquals(0., LegDistance.nearestToRadial(point, flyableLeg), .01);
  }

  private FlyableLeg flyableLeg(PathTerminator pathTerminator, ReferencedCourse outboundCourse) {
    Leg leg = Leg.builder(pathTerminator, 1)
        .recommendedNavaid(NAVAID)
        .theta(MAGNETIC_THETA)
        .outboundCourse(outboundCourse)
        .build();
    Route route = Route.newRoute(List.of(leg), new Object());
    return new FlyableLeg(null, leg, null, route);
  }

  private ConformablePoint pointOnTrueRadial() {
    ConformablePoint point = mock(ConformablePoint.class);
    when(point.latLong()).thenReturn(NAVAID.latLong().projectOut(TRUE_COURSE, 10.));
    when(point.trueCourse()).thenReturn(Optional.of(TRUE_COURSE));
    return point;
  }
}
