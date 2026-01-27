package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;

import com.google.common.collect.Range;

class TestCaFeatureExtractor {

  @Test
  void testFeatureExtraction() {

    MagneticVariation magvar = MagneticVariation.ofDegrees(0.);

    Fix navaid = mock(Fix.class);
    when(navaid.magneticVariation()).thenReturn(Optional.of(magvar));

    Leg va = mock(Leg.class);
    when(va.outboundMagneticCourse()).thenReturn(Optional.of(100.));
    when(va.altitudeConstraint()).thenReturn(Range.atLeast(8000.));
    when(va.recommendedNavaid()).thenReturn((Optional) Optional.of(navaid));

    FlyableLeg consecutiveLegs = mock(FlyableLeg.class);
    when(consecutiveLegs.current()).thenReturn(va);

    ConformablePoint point1 = mock(ConformablePoint.class);
    when(point1.trueCourse()).thenReturn(Optional.of(100.0));
    when(point1.pressureAltitude()).thenReturn(Optional.of(7500.0));

    ConformablePoint point2 = mock(ConformablePoint.class);
    when(point2.trueCourse()).thenReturn(Optional.of(110.0));
    when(point2.pressureAltitude()).thenReturn(Optional.of(8100.0));

    ViterbiFeatureVector featureVector1 = CaFeatureExtractor.INSTANCE.get().apply(point1, consecutiveLegs);
    ViterbiFeatureVector featureVector2 = CaFeatureExtractor.INSTANCE.get().apply(point2, consecutiveLegs);

    assertAll(
        () -> assertEquals(0., featureVector1.featureValue(CaFeatureExtractor.DEGREES_OFF_COURSE), 1.),
        () -> assertEquals(-500., featureVector1.featureValue(CaFeatureExtractor.FEET_PAST_TARGET_ALTITUDE), 1.),
        () -> assertEquals(10., featureVector2.featureValue(CaFeatureExtractor.DEGREES_OFF_COURSE), 1.),
        () -> assertEquals(100., featureVector2.featureValue(CaFeatureExtractor.FEET_PAST_TARGET_ALTITUDE), 1.)
    );
  }

}
