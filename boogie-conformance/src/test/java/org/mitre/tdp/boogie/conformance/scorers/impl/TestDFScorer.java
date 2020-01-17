package org.mitre.tdp.boogie.conformance.scorers.impl;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.scorers.ConsecutiveLegs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDFScorer {

  @Test
  public void testFailPointWithNoCourse() {
    DFScorer scorer = scorer();
    assertThrows(MissingRequiredFieldException.class, () -> scorer.score(dummyPoint()));
  }

  @Test
  public void test15nmOnCourseScore() {
    DFScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);

    LatLong loc = DF().pathTerminator().projectOut(0.0, 15.0).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.trueCourse()).thenReturn(Optional.of(180.0));

    when(point.courseInDegrees(any())).thenCallRealMethod();
    when(point.distanceInNmTo(any())).thenCallRealMethod();

    double score = scorer.score(point);
    assertEquals(0.4, score, 0.05);
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
    when(navaid.courseInDegrees(any())).thenCallRealMethod();
    when(navaid.distanceInNmTo(any())).thenCallRealMethod();
    when(navaid.projectOut(any(), any())).thenCallRealMethod();

    Leg DF = mock(Leg.class);
    when(DF.type()).thenReturn(LegType.DF);
    when(DF.pathTerminator()).thenReturn(navaid);

    return DF;
  }

  private DFScorer scorer() {
    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    Leg VA = VA();
    Leg DF = DF();
    when(legs.from()).thenReturn(VA);
    when(legs.to()).thenReturn(DF);
    return new DFScorer(legs);
  }
}
