package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import com.google.common.collect.Range;
import nl.jqno.equalsverifier.EqualsVerifier;

class LegTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Leg.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testLeg())
        .verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Leg.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Leg.record("WHATEVER", testLeg()))
        .verify();
  }

  @Test
  void test_Builder() {

    Fix fix = Fix.builder()
        .fixIdentifier("F")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ZERO)
        .build();

    Leg leg = Leg.builder(PathTerminator.TF, 10)
        .associatedFix(fix)
        .arcRadius(29.)
        .build();

    assertAll(
        () -> assertEquals(Range.all(), leg.altitudeConstraint(), "Should be all by default."),
        () -> assertEquals(Range.all(), leg.speedConstraint(), "Should be all by default."),
        () -> assertEquals(29., leg.arcRadius().orElse(0.))
    );
  }

  @Test
  void testOutboundCourseReferenceAndToBuilder() {
    Leg.Standard magnetic = Leg.builder(PathTerminator.CA, 1)
        .outboundMagneticCourse(94.0)
        .build();
    Leg.Standard truth = Leg.builder(PathTerminator.CA, 1)
        .outboundTrueCourse(94.0)
        .build();

    assertAll(
        () -> assertEquals(94.0, magnetic.outboundMagneticCourse().orElseThrow()),
        () -> assertFalse(magnetic.outboundTrueCourse().isPresent()),
        () -> assertEquals(ReferencedCourse.magnetic(94.0), magnetic.outboundCourse().orElseThrow()),
        () -> assertFalse(truth.outboundMagneticCourse().isPresent()),
        () -> assertEquals(94.0, truth.outboundTrueCourse().orElseThrow()),
        () -> assertEquals(ReferencedCourse.trueCourse(94.0), truth.outboundCourse().orElseThrow()),
        () -> assertEquals(truth, truth.toBuilder().build()),
        () -> assertNotEquals(magnetic, truth)
    );
  }

  @Test
  void testOutboundCourseBuilderSettersReplaceThePreviousReference() {
    Leg leg = Leg.builder(PathTerminator.CA, 1)
        .outboundTrueCourse(94.0)
        .outboundMagneticCourse(95.0)
        .build();

    assertAll(
        () -> assertEquals(ReferencedCourse.magnetic(95.0), leg.outboundCourse().orElseThrow()),
        () -> assertEquals(95.0, leg.outboundMagneticCourse().orElseThrow()),
        () -> assertTrue(leg.outboundTrueCourse().isEmpty())
    );
  }

  @Test
  void testRecordDelegatesTrueOutboundCourse() {
    Leg.Standard delegate = Leg.builder(PathTerminator.CA, 1)
        .outboundTrueCourse(94.0)
        .build();
    Leg.Record<String> record = Leg.record("source", delegate);

    assertAll(
        () -> assertEquals(delegate.outboundCourse(), record.outboundCourse()),
        () -> assertEquals(delegate.outboundTrueCourse(), record.outboundTrueCourse()),
        () -> assertTrue(record.outboundMagneticCourse().isEmpty())
    );
  }

  @Test
  void testLegacyImplementationGetsCanonicalMagneticCourseFromDefaultMethod() {
    Leg legacy = mock(Leg.class, CALLS_REAL_METHODS);
    when(legacy.outboundMagneticCourse()).thenReturn(Optional.of(94.0));

    assertAll(
        () -> assertTrue(legacy.outboundTrueCourse().isEmpty()),
        () -> assertEquals(ReferencedCourse.magnetic(94.0), legacy.outboundCourse().orElseThrow())
    );
  }

  private Leg.Standard testLeg() {
    return Leg.builder(PathTerminator.AF, 10)
        .associatedFix(Fix.builder()
            .fixIdentifier("FIX")
            .latLong(LatLong.of(0., 0.))
            .magneticVariation(MagneticVariation.ZERO)
            .build())
        .recommendedNavaid(Fix.builder()
            .fixIdentifier("NAVAID")
            .latLong(LatLong.of(0., 0.))
            .magneticVariation(MagneticVariation.ZERO)
            .build())
        .centerFix(Fix.builder()
            .fixIdentifier("CENTER")
            .latLong(LatLong.of(0., 0.))
            .magneticVariation(MagneticVariation.ZERO)
            .build())
        .rho(10.)
        .rnp(10.)
        .holdTime(Duration.ofSeconds(10))
        .isFlyOverFix(true)
        .isPublishedHoldingFix(true)
        .outboundMagneticCourse(10.)
        .routeDistance(10.)
        .theta(10.)
        .routeDistance(10.)
        .arcRadius(15.)
        .build();
  }
}
