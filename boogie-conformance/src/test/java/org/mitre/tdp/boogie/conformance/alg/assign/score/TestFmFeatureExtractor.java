package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;

public class TestFmFeatureExtractor {
  static Fix sehag = Fix.builder()
      .fixIdentifier("SEHAG")
      .latLong(LatLong.of(42.4680, -70.7851))
      .build();
  static Fix con = Fix.builder()
      .fixIdentifier("CON")
      .latLong(LatLong.of(43.2198, -71.5755))
      .magneticVariation(MagneticVariation.ofDegrees(-15.0))
      .build();
  static Leg fmLeg = Leg.builder(PathTerminator.FM, 30)
      .associatedFix(sehag)
      .recommendedNavaid(con)
      .outboundMagneticCourse(93.0)
      .theta(156.3)
      .rho(57.0)
      .build();
  static FlyableLeg flyableLeg = new FlyableLeg(null, fmLeg, null);
  static FmFeatureExtractor extractor = FmFeatureExtractor.INSTANCE;
  static FmFeatureScorer scorer = new FmFeatureScorer();

  private static ConformablePoint onPoint() {
    Double course = 78.0;
    LatLong onLeg = sehag.latLong().projectOut(course, 10.0); //to  be  clear what this point is
    ConformablePoint point = mock(ConformablePoint.class);
    when(point.latLong()).thenReturn(onLeg);
    when(point.latitude()).thenReturn(onLeg.latitude());
    when(point.longitude()).thenReturn(onLeg.longitude());
    when(point.trueCourse()).thenReturn(Optional.of(course));
    return point;
  }

  private static ConformablePoint beforePoint() {
    Double course = 258.0;
    LatLong onLeg = sehag.latLong().projectOut(course, 10.0); //to  be  clear what this point is
    ConformablePoint point = mock(ConformablePoint.class);
    when(point.latLong()).thenReturn(onLeg);
    when(point.latitude()).thenReturn(onLeg.latitude());
    when(point.longitude()).thenReturn(onLeg.longitude());
    when(point.trueCourse()).thenReturn(Optional.of(course));
    return point;
  }

  private static ConformablePoint afterPoint() {
    Double course = 78.0;
    LatLong onLeg = sehag.latLong().projectOut(course, 30.0); //to  be  clear what this point is
    ConformablePoint point = mock(ConformablePoint.class);
    when(point.latLong()).thenReturn(onLeg);
    when(point.latitude()).thenReturn(onLeg.latitude());
    when(point.longitude()).thenReturn(onLeg.longitude());
    when(point.trueCourse()).thenReturn(Optional.of(course));
    return point;
  }

  private static ConformablePoint turned() {
    Double course = 68.0;
    LatLong onLeg = sehag.latLong().projectOut(course, 10.0); //to  be  clear what this point is
    ConformablePoint point = mock(ConformablePoint.class);
    when(point.latLong()).thenReturn(onLeg);
    when(point.latitude()).thenReturn(onLeg.latitude());
    when(point.longitude()).thenReturn(onLeg.longitude());
    when(point.trueCourse()).thenReturn(Optional.of(course));
    return point;
  }

  @Test
  void onLeg() {
    ViterbiFeatureVector vector = extractor.get().apply(onPoint(), flyableLeg);
    Double score = scorer.apply(vector);

    UnaryOperator<Double> offCourseWeight = simpleLogistic(15., 30.);
    UnaryOperator<Double> distanceFromFixWeight = simpleLogistic(15, 25.);
    UnaryOperator<Double> beforeFixWeight = (d) -> d < 0.0 ? 0.0 : 1.0;

    Double crsW = offCourseWeight.apply(vector.asMap().get(FmFeatureExtractor.DISTANCE_FROM_FIX));
    Double dW = distanceFromFixWeight.apply(vector.asMap().get(FmFeatureExtractor.DISTANCE_FROM_FIX));
    Double bW = beforeFixWeight.apply(vector.asMap().get(FmFeatureExtractor.DISTANCE_BEFORE_FIX));

    assertAll("easy on course",
        () -> assertEquals(10, vector.asMap().get(FmFeatureExtractor.DISTANCE_FROM_FIX), .0001),
        () -> assertEquals(0.0, vector.asMap().get(FmFeatureExtractor.DEGREES_OFF_COURSE), .0001),
        () -> assertEquals(0.0, vector.asMap().get(FmFeatureExtractor.DISTANCE_BEFORE_FIX), .0001),
        () -> assertEquals(.77, score, .01)
    );
  }

  @Test
  void beforeLeg() {
    ViterbiFeatureVector vector = extractor.get().apply(beforePoint(), flyableLeg);
    Double score = scorer.apply(vector);
    assertAll("easy on course",
        () -> assertEquals(10, vector.asMap().get(FmFeatureExtractor.DISTANCE_FROM_FIX), .0001),
        () -> assertEquals(180.0, vector.asMap().get(FmFeatureExtractor.DEGREES_OFF_COURSE), .0001),
        () -> assertEquals(-10, vector.asMap().get(FmFeatureExtractor.DISTANCE_BEFORE_FIX), .0001),
        () -> assertEquals(0.0, score, .01)
    );
  }

  @Test
  void after() {
    ViterbiFeatureVector vector = extractor.get().apply(afterPoint(), flyableLeg);
    Double score = scorer.apply(vector);
    assertAll("easy on course",
        () -> assertEquals(30, vector.asMap().get(FmFeatureExtractor.DISTANCE_FROM_FIX), .0001),
        () -> assertEquals(0.0, vector.asMap().get(FmFeatureExtractor.DEGREES_OFF_COURSE), .0001),
        () -> assertEquals(0.0, vector.asMap().get(FmFeatureExtractor.DISTANCE_BEFORE_FIX), .0001),
        () -> assertEquals(0.011, score, .001)
    );
  }

  @Test
  void nearButDiffCourse() {
    ViterbiFeatureVector vector = extractor.get().apply(turned(), flyableLeg);
    Double score = scorer.apply(vector);
    assertAll("easy on course",
        () -> assertEquals(10, vector.asMap().get(FmFeatureExtractor.DISTANCE_FROM_FIX), .0001),
        () -> assertEquals(10.0, vector.asMap().get(FmFeatureExtractor.DEGREES_OFF_COURSE), .0001),
        () -> assertEquals(0.0, vector.asMap().get(FmFeatureExtractor.DISTANCE_BEFORE_FIX), .0001),
        () -> assertEquals(.591, score, .001)
    );
  }
}
