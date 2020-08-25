package org.mitre.tdp.boogie.conformance.alg.assign;

import com.google.common.collect.Maps;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assemble.GraphicalLegReducer;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.HmmState;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.HmmTransition;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  private final DownstreamFlyableLegResolver downstreamFlyableLegResolver;
  /**
   * The configured list of all available {@link FlyableLegState}s for the assignment.
   */
  private final Map<FlyableLeg, FlyableLegState> availableStates;

  private ScoreBasedRouteResolver(DownstreamFlyableLegResolver downstreamFlyableLegResolver, List<FlyableLeg> consecutiveLegs) {
    this.downstreamFlyableLegResolver = downstreamFlyableLegResolver;
    this.availableStates = consecutiveLegs.stream().collect(Collectors.toMap(Function.identity(), FlyableLegState::new));
  }

  public NavigableMap<ConformablePoint, ViterbiTagger.ScoredState<FlyableLegState>> resolveAssignedStates(List<? extends ConformablePoint> conformablePoints) {
    return new ViterbiTagger<>(conformablePoints, availableStates.values()).optimalScoredPath();
  }

  public NavigableMap<ConformablePoint, FlyableLeg> resolveRoute(List<? extends ConformablePoint> conformablePoints) {
    NavigableMap<ConformablePoint, ViterbiTagger.ScoredState<FlyableLegState>> z = resolveAssignedStates(conformablePoints);
    return Maps.transformValues(z, scoredState -> scoredState.state().flyableLeg());
  }

  /**
   * Internal wrapper class for state associated with a given configured {@link FlyableLeg} object. Implements dynamic programmer
   * state interface and allows access to the {@link DownstreamFlyableLegResolver} which gives the set of candidate transitions
   * from any given {@link FlyableLegState}.
   */
  public class FlyableLegState implements HmmState<ConformablePoint> {

    private FlyableLeg flyableLeg;

    public FlyableLegState(FlyableLeg flyableLeg) {
      this.flyableLeg = flyableLeg;
    }

    public FlyableLeg flyableLeg() {
      return flyableLeg;
    }

    @Override
    public double getValue(ConformablePoint stage) {
      Double res = flyableLeg.onLegScorer().score(stage, flyableLeg).orElse(Double.MIN_VALUE);
      return res;
    }

    /**
     * The set of all possible transitions from the given state - this is taken to be all downstream legs as well as the current leg.
     */
    @Override
    public List<HmmTransition<ConformablePoint, FlyableLegState>> getPossibleTransitions(ConformablePoint stage) {
      // include the downstream legs as well as the current leg as valid transition targets
      List<FlyableLeg> downstreamLegs = downstreamFlyableLegResolver.downstreamLegsOf(flyableLeg());
      List<HmmTransition<ConformablePoint, FlyableLegState>> res = Stream.concat(downstreamLegs.stream(), Stream.of(flyableLeg()))
          .distinct()
          .map(legs -> new FlyableLegTransition(
              availableStates.get(legs),
              flyableLeg().legTransitionScorer().transitionScore(stage, flyableLeg(), legs)))
          .collect(Collectors.toList());
      return res;
    }

    @Override
    public String toString() {
      return flyableLeg().toString();
    }
  }

  public static class FlyableLegTransition implements HmmTransition<ConformablePoint, FlyableLegState> {

    private final FlyableLegState nextState;
    private final Double probability;

    FlyableLegTransition(FlyableLegState nextState, Double probability) {
      this.nextState = nextState;
      this.probability = probability;
    }

    @Override
    public FlyableLegState getTransition() {
      return nextState;
    }

    @Override
    public Double getTransitionProbability() {
      return probability;
    }

    @Override
    public String toString() {
      return "nextState=" + nextState.flyableLeg() +
          ",probability=" + probability;
    }
  }

  public static ScoreBasedRouteResolver withConformableLegs(List<FlyableLeg> flyableLegs) {
    return new ScoreBasedRouteResolver(DownstreamFlyableLegResolver.withFlyableLegs(flyableLegs), flyableLegs);
  }

  /**
   * Creates a new {@link ScoreBasedRouteResolver} which will assign an input collection of {@link ConformablePoint}s to the provided
   * collection of legs.
   */
  public static ScoreBasedRouteResolver fromLegPairs(List<? extends LegPair> routeLegs) {
    return withConformableLegs(GraphicalLegReducer.with(routeLegs).flyableLegs());
  }
}
