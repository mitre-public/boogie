package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.DynamicProgrammer;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * The maximal total score assigner uses the {@link DynamicProgrammer} determining the path the aircraft intended to
 * take through a collection of legs to be the one which - on the whole - maximizes the overall conformance score as
 * returned by the {@link LegScorer}s.
 *
 * <p>To use this class for {@link LegAssigner} it is required that the full observed path of the aircraft be provided
 * as well as the full collection of legs. This class internally pre-computes the assignment and then serves it up by
 * point.
 *
 * <p>Typically this should be used when comparing the track against a smaller scoped set of legs (due to the intensity
 * of the {@link DynamicProgrammer} computation).
 */
public interface MaximalTotalScoreAssigner extends PrecomputedAssigner {

  /**
   * Pre-computes the collection of leg assignments based off of the Dynamic programmer's minimization of the total error
   * in CTD between the points and the collection of available legs.
   */
  @Override
  default Map<ConformablePoint, ConsecutiveLegs> assignments(Collection<? extends ConformablePoint> allPoints, Collection<? extends ConsecutiveLegs> allLegs) {
    List<ConsecutiveLegs> legList = new ArrayList<>(allLegs);
    List<ConformablePoint> pointList = new ArrayList<>(allPoints);
    return ScoreBasedRouteResolver.with(legList).resolveRoute(pointList);
  }
}
