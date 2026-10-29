package org.mitre.tdp.boogie.alg.facade;

import com.google.common.collect.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BinaryOperator;

import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;

/**
 * How the altitude or speed constraints of two legs terminating at the same fix combine, based on which restrictions the pilot
 * is cleared to fly when crossing it.
 * <br>
 * Each constant is applied as {@code (previous, next)}. When either constraint is {@link Range#all()} every policy returns the
 * other, they differ only when both legs are constrained:
 * <pre>
 * previous   next        PREVIOUS_SUPERSEDES  NEXT_SUPERSEDES  BOTH_APPLY
 * [3, 8]     [5, 10]     [3, 8]               [5, 10]          [5, 8]
 * (-inf, 8]  [5, +inf)   (-inf, 8]            [5, +inf)        [5, 8]
 * [6, +inf)  (-inf, 5]   [6, +inf)            (-inf, 5]        [6, +inf)
 * </pre>
 */
enum ConstraintMerge implements BinaryOperator<Range<Double>> {
  /**
   * The previous procedure governs the fix, e.g. a SID joining an airway.
   */
  PREVIOUS_SUPERSEDES {
    @Override
    public Range<Double> apply(Range<Double> previous, Range<Double> next) {
      return isBounded(previous) ? previous : next;
    }
  },
  /**
   * The procedure being joined governs the fix, e.g. an approach clearance supersedes the STAR's restrictions.
   */
  NEXT_SUPERSEDES {
    @Override
    public Range<Double> apply(Range<Double> previous, Range<Double> next) {
      return isBounded(next) ? next : previous;
    }
  },
  /**
   * Both procedures are in force at the fix, e.g. nothing supersedes the SID when joining a STAR, so the most restrictive
   * combination is kept.
   * <br>
   * When the two share no common value the procedures weren't coordinated, so the previous constraint (the clearance in force
   * while crossing the fix) is kept.
   */
  BOTH_APPLY {
    @Override
    public Range<Double> apply(Range<Double> previous, Range<Double> next) {
      if (previous.isConnected(next)) {
        Range<Double> intersection = previous.intersection(next);
        if (!intersection.isEmpty()) {
          return intersection;
        }
      }
      LOG.debug("Conflicting constraints {} and {} at a shared fix, keeping {}.", previous, next, previous);
      return previous;
    }
  };

  private static final Logger LOG = LoggerFactory.getLogger(ConstraintMerge.class);

  /**
   * Selects the policy for the pair of legs - {@link #BOTH_APPLY} for SID to STAR, otherwise whichever leg is the
   * {@link PreferredLeg} supersedes the other.
   */
  static ConstraintMerge between(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    if (SID.equals(previous.elementType()) && STAR.equals(next.elementType())) {
      return BOTH_APPLY;
    }
    return PreferredLeg.INSTANCE.apply(previous, next) == next ? NEXT_SUPERSEDES : PREVIOUS_SUPERSEDES;
  }

  private static boolean isBounded(Range<Double> range) {
    return range.hasLowerBound() || range.hasUpperBound();
  }
}
