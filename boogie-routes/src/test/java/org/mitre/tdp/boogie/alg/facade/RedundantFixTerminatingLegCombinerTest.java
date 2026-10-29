package org.mitre.tdp.boogie.alg.facade;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.FIX;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.google.common.collect.Range;

class RedundantFixTerminatingLegCombinerTest {

  private static final RedundantFixTerminatingLegCombiner COMBINER = new RedundantFixTerminatingLegCombiner();
  private static final Fix JOIN = Fix.builder().fixIdentifier("JOIN").latLong(LatLong.of(0., 0.)).build();

  @ParameterizedTest(name = "{0}")
  @MethodSource("constraintChoices")
  void keepsBoundedPreferredConstraintsOrFallsBackToTheOtherLeg(
      String description, Range<Double> other, Range<Double> preferred, Range<Double> expected) {
    assertConstraints(AIRWAY, STAR, other, preferred, expected);
    assertConstraints(SID, AIRWAY, preferred, other, expected);
  }

  private static Stream<Arguments> constraintChoices() {
    return Stream.of(
        Arguments.of("preferred overlapping range", Range.closed(3., 8.), Range.closed(5., 10.), Range.closed(5., 10.)),
        Arguments.of("preferred range despite narrower other range", Range.closed(4., 6.), Range.closed(3., 8.), Range.closed(3., 8.)),
        Arguments.of("preferred lower bound", Range.atLeast(5.), Range.atLeast(3.), Range.atLeast(3.)),
        Arguments.of("preferred upper bound", Range.atMost(5.), Range.atMost(8.), Range.atMost(8.)),
        Arguments.of("preferred complementary bound", Range.atLeast(3.), Range.atMost(8.), Range.atMost(8.)),
        Arguments.of("bounded preferred with unconstrained other", Range.all(), Range.closed(3., 8.), Range.closed(3., 8.)),
        Arguments.of("fallback closed range", Range.closed(3., 8.), Range.all(), Range.closed(3., 8.)),
        Arguments.of("fallback open range", Range.open(3., 8.), Range.all(), Range.open(3., 8.)),
        Arguments.of("fallback closed-open range", Range.closedOpen(3., 8.), Range.all(), Range.closedOpen(3., 8.)),
        Arguments.of("fallback open-closed range", Range.openClosed(3., 8.), Range.all(), Range.openClosed(3., 8.)),
        Arguments.of("fallback inclusive lower bound", Range.atLeast(3.), Range.all(), Range.atLeast(3.)),
        Arguments.of("fallback exclusive lower bound", Range.greaterThan(3.), Range.all(), Range.greaterThan(3.)),
        Arguments.of("fallback inclusive upper bound", Range.atMost(8.), Range.all(), Range.atMost(8.)),
        Arguments.of("fallback exclusive upper bound", Range.lessThan(8.), Range.all(), Range.lessThan(8.)),
        Arguments.of("both unconstrained", Range.all(), Range.all(), Range.all()),
        Arguments.of("preferred exclusive bounds", Range.closed(3., 8.), Range.open(3., 10.), Range.open(3., 10.))
    );
  }

  private static void assertConstraints(
      ElementType previousType, ElementType nextType, Range<Double> previous, Range<Double> next, Range<Double> expected) {
    ExpandedRouteLeg altitude = merge(
        leg(previousType, 10, previous, Range.all()),
        leg(nextType, 20, next, Range.all()));
    ExpandedRouteLeg speed = merge(
        leg(previousType, 10, Range.all(), previous),
        leg(nextType, 20, Range.all(), next));

    assertAll(
        () -> assertEquals(expected, altitude.altitudeConstraint()),
        () -> assertEquals(Range.all(), altitude.speedConstraint()),
        () -> assertEquals(expected, speed.speedConstraint()),
        () -> assertEquals(Range.all(), speed.altitudeConstraint())
    );
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("conflictingConstraints")
  void keepsThePreferredNextLegConstraintWhenThereIsNoCommonValue(
      String description, Range<Double> previous, Range<Double> next) {
    assertConstraints(AIRWAY, STAR, previous, next, next);
    assertConstraints(AIRWAY, STAR, next, previous, previous);
  }

  private static Stream<Arguments> conflictingConstraints() {
    return Stream.of(
        Arguments.of("disjoint ranges", Range.closed(3., 5.), Range.closed(6., 8.)),
        Arguments.of("touching exclusive endpoint", Range.closedOpen(3., 5.), Range.closed(5., 8.))
    );
  }

  @Test
  void keepsThePreferredPreviousLegConstraintsWhenThereIsNoCommonValue() {
    ExpandedRouteLeg sid = leg(SID, 10, Range.atLeast(6000.), Range.atMost(220.));
    ExpandedRouteLeg airway = leg(AIRWAY, 20, Range.atMost(3000.), Range.atLeast(250.));

    ExpandedRouteLeg result = merge(sid, airway);

    assertAll(
        () -> assertEquals(sid.altitudeConstraint(), result.altitudeConstraint()),
        () -> assertEquals(sid.speedConstraint(), result.speedConstraint())
    );
  }

  @Test
  void fallsBackIndependentlyForAltitudeAndSpeed() {
    ExpandedRouteLeg fallbackAltitude = merge(
        leg(AIRWAY, 10, Range.atLeast(3000.), Range.atMost(220.)),
        leg(STAR, 20, Range.all(), Range.atLeast(250.)));
    ExpandedRouteLeg fallbackSpeed = merge(
        leg(AIRWAY, 10, Range.atMost(3000.), Range.atLeast(180.)),
        leg(STAR, 20, Range.atLeast(6000.), Range.all()));

    assertAll(
        () -> assertEquals(Range.atLeast(3000.), fallbackAltitude.altitudeConstraint()),
        () -> assertEquals(Range.atLeast(250.), fallbackAltitude.speedConstraint()),
        () -> assertEquals(Range.atLeast(6000.), fallbackSpeed.altitudeConstraint()),
        () -> assertEquals(Range.atLeast(180.), fallbackSpeed.speedConstraint())
    );
  }

  @Test
  void repeatedMergesKeepPreferredConstraintsAndTheOriginalPath() {
    ExpandedRouteLeg sid = leg(SID, 10, Range.atLeast(3000.), Range.atMost(250.));
    ExpandedRouteLeg star = leg(STAR, 20, Range.all(), Range.atMost(220.));
    ExpandedRouteLeg fix = leg(FIX, 30, Range.atMost(6000.), Range.all());
    List<ExpandedRouteLeg> merged = COMBINER.apply(List.of(sid, star, fix));

    assertEquals(1, merged.size());
    ExpandedRouteLeg result = merged.get(0);
    assertAll(
        () -> assertEquals(Range.atLeast(3000.), result.altitudeConstraint()),
        () -> assertEquals(Range.atMost(220.), result.speedConstraint()),
        () -> assertEquals(sid.pathTerminator(), result.pathTerminator()),
        () -> assertEquals(sid.outboundMagneticCourse(), result.outboundMagneticCourse()),
        () -> assertEquals(sid.sequenceNumber(), result.sequenceNumber())
    );
  }

  @Test
  void keepsThePreviousPathRnpOrFallsBackToTheNextLeg() {
    assertAll(
        () -> assertEquals(Optional.of(2.0), merge(rnpLeg(AIRWAY, 10, 2.0), rnpLeg(STAR, 20, 1.0)).rnp()),
        () -> assertEquals(Optional.of(1.0), merge(rnpLeg(AIRWAY, 10, null), rnpLeg(STAR, 20, 1.0)).rnp()),
        () -> assertEquals(Optional.of(1.0), merge(rnpLeg(SID, 10, 1.0), rnpLeg(AIRWAY, 20, 2.0)).rnp()),
        () -> assertEquals(Optional.of(2.0), merge(rnpLeg(SID, 10, null), rnpLeg(AIRWAY, 20, 2.0)).rnp()),
        () -> assertEquals(Optional.empty(), merge(rnpLeg(AIRWAY, 10, null), rnpLeg(STAR, 20, null)).rnp())
    );
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("sidToStarConstraints")
  void sidToStarKeepsTheMostRestrictiveConstraintOrTheSidOnConflict(
      String description, Range<Double> sid, Range<Double> star, Range<Double> expected) {
    assertConstraints(SID, STAR, sid, star, expected);
  }

  private static Stream<Arguments> sidToStarConstraints() {
    return Stream.of(
        Arguments.of("overlapping ranges", Range.closed(3., 8.), Range.closed(5., 10.), Range.closed(5., 8.)),
        Arguments.of("sid ceiling with star floor", Range.atMost(8.), Range.atLeast(5.), Range.closed(5., 8.)),
        Arguments.of("tighter sid ceiling", Range.atMost(5.), Range.atMost(8.), Range.atMost(5.)),
        Arguments.of("unconstrained star", Range.atLeast(3.), Range.all(), Range.atLeast(3.)),
        Arguments.of("unconstrained sid", Range.all(), Range.atMost(8.), Range.atMost(8.)),
        Arguments.of("both unconstrained", Range.all(), Range.all(), Range.all()),
        Arguments.of("disjoint ranges keep sid", Range.atLeast(6.), Range.atMost(5.), Range.atLeast(6.)),
        Arguments.of("touching exclusive endpoint keeps sid", Range.closedOpen(3., 5.), Range.closed(5., 8.), Range.closedOpen(3., 5.))
    );
  }

  @Test
  void keepsThePreferredFlyOver() {
    assertAll(
        () -> assertTrue(merge(flyOverLeg(AIRWAY, 10, false), flyOverLeg(STAR, 20, true)).isFlyOverFix()),
        () -> assertFalse(merge(flyOverLeg(AIRWAY, 10, true), flyOverLeg(STAR, 20, false)).isFlyOverFix()),
        () -> assertTrue(merge(flyOverLeg(SID, 10, true), flyOverLeg(AIRWAY, 20, false)).isFlyOverFix()),
        () -> assertFalse(merge(flyOverLeg(SID, 10, false), flyOverLeg(AIRWAY, 20, true)).isFlyOverFix())
    );
  }

  private static ExpandedRouteLeg merge(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    List<ExpandedRouteLeg> merged = COMBINER.apply(List.of(previous, next));
    assertEquals(1, merged.size());
    return merged.get(0);
  }

  private static ExpandedRouteLeg leg(ElementType type, int sequence, Range<Double> altitude, Range<Double> speed) {
    return new ExpandedRouteLeg(type.name(), type, "", Leg.cfBuilder(JOIN, sequence, (double) sequence)
        .altitudeConstraint(altitude)
        .speedConstraint(speed)
        .build());
  }

  private static ExpandedRouteLeg rnpLeg(ElementType type, int sequence, Double rnp) {
    return new ExpandedRouteLeg(type.name(), type, "", Leg.cfBuilder(JOIN, sequence, (double) sequence)
        .rnp(rnp)
        .build());
  }

  private static ExpandedRouteLeg flyOverLeg(ElementType type, int sequence, boolean flyOver) {
    return new ExpandedRouteLeg(type.name(), type, "", Leg.cfBuilder(JOIN, sequence, (double) sequence)
        .isFlyOverFix(flyOver)
        .build());
  }
}
