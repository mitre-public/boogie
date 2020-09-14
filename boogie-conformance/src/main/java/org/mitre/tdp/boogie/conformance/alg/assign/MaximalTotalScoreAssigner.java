package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.List;
import java.util.NavigableMap;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTrellis;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * The maximal total score assigner uses the {@link ViterbiTagger} determining the path the aircraft intended to
 * take through a collection of legs to be the one which - on the whole - maximizes the overall conformance score as
 * returned by the {@link OnLegScorer}s.
 *
 * <p>To use this class it is required that the full observed path of the aircraft be provided as well as the full
 * collection of legs. This class internally pre-computes the assignment and then serves it up by point.
 *
 * <p>Typically this should be used when comparing the track against a smaller scoped set of legs (due to the intensity
 * of the {@link ViterbiTagger} computation).
 */
public class MaximalTotalScoreAssigner {

  private final List<FlyableLeg> legList;
  private final List<? extends ConformablePoint> pointList;

  public MaximalTotalScoreAssigner(List<FlyableLeg> legList, List<? extends ConformablePoint> pointList) {
    this.legList = legList;
    this.pointList = pointList;
  }

  /**
   * Pre-computes the collection of leg assignments based off of the Dynamic programmer's minimization of the total error
   * in CTD between the points and the collection of available legs.
   */
  public NavigableMap<ConformablePoint, FlyableLeg> assignments() {
    return ScoreBasedRouteResolver.withFlyableLegs(legList).resolveRoute(pointList);
  }

  public ViterbiTrellis<ConformablePoint, FlyableLeg> trellis() {
    return ScoreBasedRouteResolver.withFlyableLegs(legList).trellis(pointList);
  }
}
