package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

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
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

public class TestDfScorer {

  @Test
  public void testFailPointWithNoCourse() {
    assertThrows(MissingRequiredFieldException.class, () -> new DfScorer().scoreAgainstLeg(dummyPoint(), conformableLegs()));
  }

  @Test
  public void test15nmOnCourseScore() {
    ConformablePoint point = mock(ConformablePoint.class);

    LatLong loc = DF().pathTerminator().projectOut(0.0, 20.0).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();
    when(point.trueCourse()).thenReturn(Optional.of(180.0));

    when(point.courseInDegrees(any())).thenCallRealMethod();
    when(point.distanceInNmTo(any())).thenCallRealMethod();
    when(point.projectOut(any(), any())).thenCallRealMethod();

    double score = new DfScorer().scoreAgainstLeg(point, conformableLegs());
    assertEquals(0.95, score, 0.05);
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
    when(DF.type()).thenReturn(PathTerm.DF);
    when(DF.pathTerminator()).thenReturn(navaid);

    return DF;
  }

  private FlyableLeg conformableLegs() {
    return new FlyableLeg(VA(), DF(), null, dummyRoute());
  }

  private Route dummyRoute(){
    return Route.newRoute(new ArrayList<>(), new Object());
  }
}
