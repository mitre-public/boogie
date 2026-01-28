package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static java.util.Optional.empty;
import static java.util.Optional.of;
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
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;

class TestAfFeatureExtractor {

  @Test
  void testFailOnMissingTheta() {
    Leg TF = TF();
    Leg AF = AF();
    when(AF.theta()).thenReturn(empty());

    FlyableLeg flyableLeg = new FlyableLeg(TF, AF, null, dummyRoute());

    assertThrows(MissingRequiredFieldException.class, () -> AfFeatureExtractor.INSTANCE.get().apply(dummyPoint(), flyableLeg));
  }

  @Test
  void testFailOnMissingRho() {
    Leg TF = TF();
    Leg AF = AF();
    when(AF.rho()).thenReturn(empty());

    FlyableLeg flyableLeg = new FlyableLeg(TF, AF, null, dummyRoute());

    assertThrows(MissingRequiredFieldException.class, () -> AfFeatureExtractor.INSTANCE.get().apply(dummyPoint(), flyableLeg));
  }

  @Test
  void testFailOnMissingOutboundCourse() {
    Leg TF = TF();
    Leg AF = AF();
    when(AF.outboundMagneticCourse()).thenReturn(empty());

    FlyableLeg flyableLeg = new FlyableLeg(TF, AF, null, dummyRoute());

    assertThrows(MissingRequiredFieldException.class, () -> AfFeatureExtractor.INSTANCE.get().apply(dummyPoint(), flyableLeg));
  }

  @Test
  void testFailOnMissingRecommendedNavaid() {
    Leg TF = TF();
    Leg AF = AF();
    when(AF.recommendedNavaid()).thenReturn(empty());

    FlyableLeg flyableLeg = new FlyableLeg(TF, AF, null, dummyRoute());

    assertThrows(MissingRequiredFieldException.class, () -> AfFeatureExtractor.INSTANCE.get().apply(dummyPoint(), flyableLeg));
  }

  @Test
  void testAFPastTerminatorScore() {
    ConformablePoint point = mock(ConformablePoint.class);

    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation().get();
    double projCourse = localVariation.magneticToTrue(AF.theta().get()) + 10.0;

    LatLong loc = AF.recommendedNavaid().get().projectOut(projCourse, AF.rho().get()).latLong();
    when(point.latLong()).thenReturn(loc);

    double radialAngleFeature = AfFeatureExtractor.INSTANCE.get().apply(point, conformableLeg()).featureValue(AfFeatureExtractor.IN_RADIAL_ANGLE);

    assertEquals(0.0, radialAngleFeature, 0.01, "Points past the end of the path terminator radial should have a 0.0 score.");
  }

  @Test
  void testAFPreBoundaryRadialScore() {
    ConformablePoint point = mock(ConformablePoint.class);

    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation().get();
    double projCourse = localVariation.magneticToTrue(AF.outboundMagneticCourse().get()) - 10.0;

    LatLong loc = AF.recommendedNavaid().get().projectOut(projCourse, AF.rho().get()).latLong();
    when(point.latLong()).thenReturn(loc);

    double radialAngleFeature = AfFeatureExtractor.INSTANCE.get().apply(point, conformableLeg()).featureValue(AfFeatureExtractor.IN_RADIAL_ANGLE);

    assertEquals(0.0, radialAngleFeature, 0.01, "Points prior to the boundary radial should have a 0.0 score.");
  }

  @Test
  void testAFInBoundaryOnArcScoreHigh() {
    ConformablePoint point = mock(ConformablePoint.class);
    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation().get();
    double halfTrueCourse = localVariation.magneticToTrue((AF.outboundMagneticCourse().get() + AF.theta().get()) / 2.0d);

    LatLong loc = AF.recommendedNavaid().get().projectOut(halfTrueCourse, AF.rho().get()).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();

    ViterbiFeatureVector vector = AfFeatureExtractor.INSTANCE.get().apply(point, conformableLeg());
    double inRadialAngle = vector.featureValue(AfFeatureExtractor.IN_RADIAL_ANGLE);
    double distanceOffTrack = vector.featureValue(AfFeatureExtractor.OFF_TRACK_DISTANCE);

    assertEquals(1., inRadialAngle, .001, "Points between the start and end of the arc should get a 1.0.");
    assertEquals(0., distanceOffTrack, .1, "Points directly on the defined arc should have a 0.0 off track distance.");
  }

  @Test
  void testAFInBoundaryOffset1NMScoreModerate() {
    ConformablePoint point = mock(ConformablePoint.class);
    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation().get();
    double halfTrueCourse = localVariation.magneticToTrue((AF.outboundMagneticCourse().get() + AF.theta().get()) / 2.0d);

    LatLong loc = AF.recommendedNavaid().get().projectOut(halfTrueCourse, AF.rho().get() - 1.0).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();

    ViterbiFeatureVector vector = AfFeatureExtractor.INSTANCE.get().apply(point, conformableLeg());
    double inRadialAngle = vector.featureValue(AfFeatureExtractor.IN_RADIAL_ANGLE);
    double distanceOffTrack = vector.featureValue(AfFeatureExtractor.OFF_TRACK_DISTANCE);

    assertEquals(1., inRadialAngle, .001);
    assertEquals(1., distanceOffTrack, .1);
  }

  @Test
  void testAFInBoundaryOffset2NMScoreLow() {
    ConformablePoint point = mock(ConformablePoint.class);
    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation().get();
    double halfTrueCourse = localVariation.magneticToTrue((AF.outboundMagneticCourse().get() + AF.theta().get()) / 2.0d);

    LatLong loc = AF.recommendedNavaid().get().projectOut(halfTrueCourse, AF.rho().get() + 2.0).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();

    ViterbiFeatureVector vector = AfFeatureExtractor.INSTANCE.get().apply(point, conformableLeg());
    double inRadialAngle = vector.featureValue(AfFeatureExtractor.IN_RADIAL_ANGLE);
    double distanceOffTrack = vector.featureValue(AfFeatureExtractor.OFF_TRACK_DISTANCE);

    assertEquals(1., inRadialAngle, .001);
    assertEquals(2., distanceOffTrack, .1);
  }

  private ConformablePoint dummyPoint() {
    return mock(ConformablePoint.class);
  }

  private Leg TF() {
    return mock(Leg.class);
  }

  private Leg AF() {
    Fix navaid = mock(Fix.class);

    LatLong ll = LatLong.of(42.479575000000004, -122.91293333333334);
    when(navaid.latLong()).thenReturn(ll);
    when(navaid.latitude()).thenCallRealMethod();
    when(navaid.longitude()).thenCallRealMethod();
    when(navaid.courseInDegrees(any())).thenCallRealMethod();
    when(navaid.distanceInNmTo(any())).thenCallRealMethod();
    when(navaid.projectOut(any(), any())).thenCallRealMethod();

    when(navaid.magneticVariation()).thenReturn((Optional) Optional.of(MagneticVariation.ofDegrees(19.)));

    TurnDirection td = mock(TurnDirection.class);
    when(td.isRight()).thenReturn(true);
    when(td.isLeft()).thenReturn(false);

    Optional<TurnDirection> otd = of(td);

    Leg AF = mock(Leg.class);
    when(AF.pathTerminator()).thenReturn(PathTerminator.AF);
    when(AF.recommendedNavaid()).thenReturn((Optional) of(navaid));
    when(AF.outboundMagneticCourse()).thenReturn(of(98.0));
    when(AF.theta()).thenReturn(of(138.0));
    when(AF.rho()).thenReturn(of(15.0));
    when(AF.turnDirection()).thenReturn((Optional) otd);

    return AF;
  }

  private FlyableLeg conformableLeg() {
    return new FlyableLeg(TF(), AF(), null, dummyRoute());
  }

  private Route dummyRoute() {
    return Route.newRoute(new ArrayList<>(), new Object());
  }
}
