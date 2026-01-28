package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;

class TestDfFeatureExtractor {

  @Test
  void testFailPointWithNoCourse() {
    assertThrows(MissingRequiredFieldException.class, () -> DfFeatureExtractor.INSTANCE.get().apply(dummyPoint(), conformableLegs()));
  }

  @Test
  void test15nmOnCourseScore() {
    ConformablePoint point = mock(ConformablePoint.class);

    LatLong loc = DF().associatedFix().orElseThrow(AssertionError::new).projectOut(0.0, 20.0).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();
    when(point.trueCourse()).thenReturn(Optional.of(180.0));

    when(point.courseInDegrees(any())).thenCallRealMethod();
    when(point.distanceInNmTo(any())).thenCallRealMethod();
    when(point.projectOut(any(), any())).thenCallRealMethod();

    ViterbiFeatureVector vector = DfFeatureExtractor.INSTANCE.get().apply(point, conformableLegs());
    assertEquals(0., vector.featureValue(DfFeatureExtractor.PROJECTED_DISTANCE_OFFSET), 1.);
  }

  private ConformablePoint dummyPoint() {
    return mock(ConformablePoint.class);
  }

  private Leg VA() {
    return mock(Leg.class);
  }

  private Leg DF() {
    Fix navaid = mock(Fix.class);

    LatLong ll = LatLong.of(44.120897222222226, -123.22283055555556);
    when(navaid.latLong()).thenReturn(ll);
    when(navaid.latitude()).thenCallRealMethod();
    when(navaid.longitude()).thenCallRealMethod();
    when(navaid.courseInDegrees(any())).thenCallRealMethod();
    when(navaid.distanceInNmTo(any())).thenCallRealMethod();
    when(navaid.projectOut(any(), any())).thenCallRealMethod();

    Leg DF = mock(Leg.class);
    when(DF.pathTerminator()).thenReturn(PathTerminator.DF);
    when(DF.associatedFix()).thenReturn((Optional) Optional.of(navaid));

    return DF;
  }

  private FlyableLeg conformableLegs() {
    return new FlyableLeg(VA(), DF(), null, dummyRoute());
  }

  private Route dummyRoute() {
    return Route.newRoute(new ArrayList<>(), new Object());
  }
}
