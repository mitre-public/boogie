package org.mitre.tdp.boogie.alg.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.alg.facade.LegMerge.BOTH_APPLY;
import static org.mitre.tdp.boogie.alg.facade.LegMerge.NEXT_GOVERNS;
import static org.mitre.tdp.boogie.alg.facade.LegMerge.PREVIOUS_GOVERNS;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.FIX;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.google.common.collect.Range;

class LegMergeTest {

  private static final Fix JOIN = Fix.builder().fixIdentifier("JOIN").latLong(LatLong.of(0., 0.)).build();

  @ParameterizedTest(name = "{0} {1} -> {2} {3}")
  @MethodSource("policies")
  void appliesTheExpectedPolicy(LegMerge policy, String description, Range<Double> previous, Range<Double> next, Range<Double> expected) {
    assertEquals(expected, policy.constraint(previous, next));
  }

  private static Stream<Arguments> policies() {
    return Stream.of(
        Arguments.of(PREVIOUS_GOVERNS, "overlapping", Range.closed(3., 8.), Range.closed(5., 10.), Range.closed(3., 8.)),
        Arguments.of(PREVIOUS_GOVERNS, "complementary", Range.atMost(8.), Range.atLeast(5.), Range.atMost(8.)),
        Arguments.of(PREVIOUS_GOVERNS, "conflicting", Range.atLeast(6.), Range.atMost(5.), Range.atLeast(6.)),
        Arguments.of(PREVIOUS_GOVERNS, "unconstrained previous", Range.all(), Range.atMost(5.), Range.atMost(5.)),

        Arguments.of(NEXT_GOVERNS, "overlapping", Range.closed(3., 8.), Range.closed(5., 10.), Range.closed(5., 10.)),
        Arguments.of(NEXT_GOVERNS, "complementary", Range.atMost(8.), Range.atLeast(5.), Range.atLeast(5.)),
        Arguments.of(NEXT_GOVERNS, "conflicting", Range.atLeast(6.), Range.atMost(5.), Range.atMost(5.)),
        Arguments.of(NEXT_GOVERNS, "unconstrained next", Range.atLeast(3.), Range.all(), Range.atLeast(3.)),

        Arguments.of(BOTH_APPLY, "overlapping", Range.closed(3., 8.), Range.closed(5., 10.), Range.closed(5., 8.)),
        Arguments.of(BOTH_APPLY, "complementary", Range.atMost(8.), Range.atLeast(5.), Range.closed(5., 8.)),
        Arguments.of(BOTH_APPLY, "conflicting", Range.atLeast(6.), Range.atMost(5.), Range.atLeast(6.)),
        Arguments.of(BOTH_APPLY, "touching exclusive endpoint", Range.closedOpen(3., 5.), Range.closed(5., 8.), Range.closedOpen(3., 5.)),
        Arguments.of(BOTH_APPLY, "unconstrained previous", Range.all(), Range.atMost(8.), Range.atMost(8.)),
        Arguments.of(BOTH_APPLY, "unconstrained next", Range.atLeast(3.), Range.all(), Range.atLeast(3.)),

        Arguments.of(PREVIOUS_GOVERNS, "both unconstrained", Range.all(), Range.all(), Range.all()),
        Arguments.of(NEXT_GOVERNS, "both unconstrained", Range.all(), Range.all(), Range.all()),
        Arguments.of(BOTH_APPLY, "both unconstrained", Range.all(), Range.all(), Range.all())
    );
  }

  @ParameterizedTest(name = "{0} to {1} -> {2}")
  @MethodSource("pairs")
  void selectsThePolicyForThePairOfLegs(ElementType previous, ElementType next, LegMerge expected) {
    assertEquals(expected, LegMerge.between(leg(previous, 10), leg(next, 20)));
  }

  private static Stream<Arguments> pairs() {
    return Stream.of(
        Arguments.of(SID, STAR, BOTH_APPLY),
        Arguments.of(SID, AIRWAY, PREVIOUS_GOVERNS),
        Arguments.of(SID, FIX, PREVIOUS_GOVERNS),
        Arguments.of(SID, APPROACH, NEXT_GOVERNS),
        Arguments.of(AIRWAY, STAR, NEXT_GOVERNS),
        Arguments.of(AIRWAY, APPROACH, NEXT_GOVERNS),
        Arguments.of(STAR, APPROACH, NEXT_GOVERNS),
        Arguments.of(FIX, AIRWAY, NEXT_GOVERNS),
        Arguments.of(AIRWAY, FIX, PREVIOUS_GOVERNS)
    );
  }

  private static ExpandedRouteLeg leg(ElementType type, int sequence) {
    return new ExpandedRouteLeg(type.name(), type, "", Leg.cfBuilder(JOIN, sequence, (double) sequence).build());
  }
}
