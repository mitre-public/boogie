package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assemble.GraphicalLegReducer;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTrellis;
import org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MinValueScorer;

/**
 * A score based route resolved is configured with an input collection of {@link FlyableLeg} which are used in conjunction with
 * an input collection of {@link ConformablePoint}s to determine (given the set of candidate legs) the path through the legs of which
 * maximises the overall score (typically a conformance score) across all of the points.
 *
 * This class then returns a mapping from each {@link ConformablePoint} to the {@link FlyableLeg} which they were assigned to in
 * maximizing the overall score across legs and points.
 */
public class ScoreBasedRouteResolver {

  /**
   * The graphical representation of all of the {@link FlyableLeg} loaded into the resolver. This graph is used to determine
   * the available transitions given any particular edge state. If the edges don't show connections a transition cannot be made.
   */
  private final FlyableLegGraph flyableLegGraph;
  /**
   * The configured list of all available {@link FlyableLeg}s for the assignment.
   */
  private final List<FlyableLeg> flyableLegs;

  private static final Leg UNKNOWN_LEG = new UnknownLeg();
  private static final FlyableLeg UNKNOWN_FLYABLE_LEG = new FlyableLeg(null, UNKNOWN_LEG, null).setOnLegScorer(new MinValueScorer()).setLegTransitionScorer((x, y) -> 1e-5);

  private ScoreBasedRouteResolver(FlyableLegGraph flyableLegGraph, List<FlyableLeg> flyableLegs) {
    this.flyableLegGraph = flyableLegGraph;
    this.flyableLegs = flyableLegs;
  }

  public NavigableMap<ConformablePoint, FlyableLeg> resolveRoute(List<? extends ConformablePoint> conformablePoints) {
    return tagger(conformablePoints).optimalPath();
  }

  public ViterbiTrellis<ConformablePoint, FlyableLeg> trellis(List<? extends ConformablePoint> conformablePoints) {
    return tagger(conformablePoints).trellis();
  }

  private ViterbiTagger<ConformablePoint, FlyableLeg> tagger(List<? extends ConformablePoint> conformablePoints) {
    return new ViterbiTagger<>(conformablePoints, flyableLegs,
        ScoreBasedRouteResolver::legScore, this::validTransitions, ScoreBasedRouteResolver::transitionScore);
  }

  private static Double legScore(ConformablePoint pt, FlyableLeg leg) {
    return leg.onLegScorer().score(pt, leg).orElse(1e-10);
  }

  private Collection<FlyableLeg> validTransitions(FlyableLeg s) {
    if (s.equals(UNKNOWN_FLYABLE_LEG)) {
      return flyableLegs;
    }
    return Stream.concat(flyableLegGraph.downstreamLegsOf(s).stream(), Stream.of(s, UNKNOWN_FLYABLE_LEG)).collect(Collectors.toList());
  }

  private static Double transitionScore(FlyableLeg l1, FlyableLeg l2) {
    if (l1 == UNKNOWN_FLYABLE_LEG) {
      return 1e-5;
    }
    return l1.legTransitionScorer().transitionScore(l1, l2);
  }

  public static ScoreBasedRouteResolver withConformableLegs(List<FlyableLeg> flyableLegs) {
    return new ScoreBasedRouteResolver(FlyableLegGraph.withFlyableLegs(flyableLegs), flyableLegs);
  }

  /**
   * Creates a new {@link ScoreBasedRouteResolver} which will assign an input collection of {@link ConformablePoint}s to the provided
   * collection of legs.
   */
  public static ScoreBasedRouteResolver fromLegPairs(List<? extends LegPair> routeLegs) {
    return withConformableLegs(GraphicalLegReducer.with(routeLegs).flyableLegs());
  }

}
