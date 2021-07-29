package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static java.util.Optional.empty;
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
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

class TestCfFeatureExtractor {

  @Test
  void testFailOnMissingOutboundMagneticCourse() {
    Leg VI = VI();
    Leg CF = CF();
    when(CF.outboundMagneticCourse()).thenReturn(empty());

    FlyableLeg flyableLeg = new FlyableLeg(VI, CF, null, dummyRoute());

    assertThrows(MissingRequiredFieldException.class, () -> CfFeatureExtractor.INSTANCE.get().apply(dummyPoint(), flyableLeg));
  }

  private ConformablePoint dummyPoint() {
    ConformablePoint pt = mock(ConformablePoint.class);
    when(pt.trueCourse()).thenReturn(Optional.of(212.));
    when(pt.latLong()).thenReturn(LatLong.of(33.87001388888889, -116.42978333333333));
    when(pt.latitude()).thenCallRealMethod();
    when(pt.longitude()).thenCallRealMethod();

    when(pt.projectOut(any(), any())).thenCallRealMethod();
    when(pt.distanceInNmTo(any())).thenCallRealMethod();
    when(pt.courseInDegrees(any())).thenCallRealMethod();
    return pt;
  }

  private Leg VI() {
    return mock(Leg.class);
  }

  private Leg CF() {
    Fix navaid = mock(Fix.class);

    LatLong ll = LatLong.of(33.87001388888889, -116.42978333333333);
    when(navaid.latLong()).thenReturn(ll);
    when(navaid.courseInDegrees(any())).thenCallRealMethod();
    when(navaid.distanceInNmTo(any())).thenCallRealMethod();
    when(navaid.projectOut(any(), any())).thenCallRealMethod();

    MagneticVariation var = new MagneticVariation(13.0, 11.482411518265646);
    when(navaid.magneticVariation()).thenReturn(var);

    Leg CF = mock(Leg.class);
    when(CF.associatedFix()).thenReturn((Optional) Optional.of(navaid));
    when(CF.pathTerminator()).thenReturn(PathTerminator.CF);
    when(CF.recommendedNavaid()).thenReturn((Optional) Optional.of(navaid));
    when(CF.outboundMagneticCourse()).thenReturn(Optional.of(199.4));
    when(CF.theta()).thenReturn(Optional.of(13.7));
    when(CF.rho()).thenReturn(Optional.of(137.2));
    when(CF.routeDistance()).thenReturn(Optional.of(2.7));

    return CF;
  }

  private FlyableLeg conformableLeg() {
    return new FlyableLeg(VI(), CF(), null, dummyRoute());
  }

  private Route dummyRoute() {
    return Route.newRoute(new ArrayList<>(), new Object());
  }
}
