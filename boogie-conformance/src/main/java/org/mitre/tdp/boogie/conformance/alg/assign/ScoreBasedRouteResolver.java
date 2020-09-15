package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTrellis;
import org.mitre.tdp.boogie.conformance.alg.assign.score.LegTransitionScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MinValueScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.impl.UnscoreableLegScorer;

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
   * Static reference for an unknown flyable leg - this is generally used to indicate when the aircraft is off the known route and
   * no better leg can be assigned.
   */
  private static final LegTransitionScorer UNKNOWN_LEG_TRANSITION_SCORER = (x, y) -> x.current().equals(UnknownLeg.getInstance()) && y.current().equals(UnknownLeg.getInstance()) ? 1. : 1e-1;

  private static final FlyableLeg UNKNOWN_FLYABLE_LEG = new FlyableLeg(null, UnknownLeg.getInstance(), null)
      .setOnLegScorer(MinValueScorer.getInstance())
      .setLegTransitionScorer(UNKNOWN_LEG_TRANSITION_SCORER);
  /**
   * The graphical representation of all of the {@link FlyableLeg} loaded into the resolver. This graph is used to determine
   * the available transitions given any particular edge state. If the edges don't show connections a transition cannot be made.
   */
  private final FlyableLegGraph flyableLegGraph;
  /**
   * The configured list of all available {@link FlyableLeg}s for the assignment.
   */
  private final List<FlyableLeg> flyableLegs;
  /**
   * Indicates whether the leg representing an unknown state should be included in the set of candidate transitionable legs.
   */
  private boolean includeUnknownLeg = true;

  private ScoreBasedRouteResolver(FlyableLegGraph flyableLegGraph, List<FlyableLeg> flyableLegs) {
    this.flyableLegGraph = flyableLegGraph;
    this.flyableLegs = flyableLegs;
  }

  public ScoreBasedRouteResolver setIncludeUnknownLeg(boolean includeUnknownLeg) {
    this.includeUnknownLeg = includeUnknownLeg;
    return this;
  }

  public NavigableMap<ConformablePoint, FlyableLeg> resolveRoute(List<? extends ConformablePoint> conformablePoints) {
    ViterbiTrellis<ConformablePoint, FlyableLeg> trellis = trellis(conformablePoints);
    return trellis.optimalPath();
  }

  public ViterbiTrellis<ConformablePoint, FlyableLeg> trellis(List<? extends ConformablePoint> conformablePoints) {
    return tagger(conformablePoints).trellis();
  }

  private ViterbiTagger<ConformablePoint, FlyableLeg> tagger(List<? extends ConformablePoint> conformablePoints) {
    return new ViterbiTagger<>(conformablePoints, allTaggableLegs(),
        ScoreBasedRouteResolver::legScore, this::validTransitions, ScoreBasedRouteResolver::transitionScore);
  }

  /**
   * Returns a copy of the input list of {@link FlyableLeg}s optionally appending the {@link #UNKNOWN_FLYABLE_LEG} when
   * specified to do so.
   */
  private List<FlyableLeg> allTaggableLegs() {
    List<FlyableLeg> allTaggableLegs = new ArrayList<>(flyableLegs);
    if (includeUnknownLeg) {
      allTaggableLegs.add(UNKNOWN_FLYABLE_LEG);
    }
    return allTaggableLegs;
  }

  private static Double legScore(ConformablePoint pt, FlyableLeg leg) {
    return leg.onLegScorer().score(pt, leg).orElse(UnscoreableLegScorer.getInstance().scoreAgainstLeg(pt, leg));
  }

  private Collection<FlyableLeg> validTransitions(FlyableLeg s) {
    if (s.equals(UNKNOWN_FLYABLE_LEG)) {
      return Stream.concat(flyableLegs.stream(), Stream.of(s)).collect(Collectors.toList());
    }
    return Stream.concat(flyableLegGraph.downstreamLegsOf(s).stream(), Stream.of(s, includeUnknownLeg ? UNKNOWN_FLYABLE_LEG : null)).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private static Double transitionScore(FlyableLeg l1, FlyableLeg l2) {
    if (l2.equals(UNKNOWN_FLYABLE_LEG)) {
      return UNKNOWN_LEG_TRANSITION_SCORER.transitionScore(l1, l2);
    }
    return l1.legTransitionScorer().transitionScore(l1, l2);
  }

  public static ScoreBasedRouteResolver withFlyableLegs(List<FlyableLeg> flyableLegs) {
    return new ScoreBasedRouteResolver(FlyableLegGraph.withFlyableLegs(flyableLegs), flyableLegs);
  }
}
