package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.DynamicProgrammer;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.DynamicProgrammerState;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.DynamicProgrammerTransition;

import com.google.common.collect.Maps;

/**
 * A score based route resolved is configured with an input collection of {@link ConsecutiveLegs} which are used in conjunction with
 * an input collection of {@link ConformablePoint}s to determine (given the set of candidate legs) the path through the legs of which
 * maximises the overall score (typically a conformance score) across all of the points.
 *
 * This class then returns a mapping from each {@link ConformablePoint} to the {@link ConsecutiveLegs} which they were assigned to in
 * maximizing the overall score across legs and points.
 */
public class ScoreBasedRouteResolver {

  /**
   * The graphical representation of all of the {@link ConsecutiveLegs} loaded into the resolver. This graph is used to determine
   * the available transitions given any particular edge state. If the edges don't show connections a transition cannot be made.
   */
  private DownstreamConsecutiveLegsResolver downstreamConsecutiveLegsResolver;
  /**
   * The configured list of all available {@link ConsecutiveLegsState}s for the assignment.
   */
  private List<ConsecutiveLegsState> availableStates;
  /**
   * The configured mapping from {@link ConsecutiveLegs} to their transition - since this lookup is static for a given leg set.
   */
  private Map<ConsecutiveLegs, DynamicProgrammerTransition<ConformablePoint, ConsecutiveLegsState>> availableTransitions;

  private ScoreBasedRouteResolver(DownstreamConsecutiveLegsResolver downstreamConsecutiveLegsResolver, List<ConsecutiveLegs> consecutiveLegs) {
    this.downstreamConsecutiveLegsResolver = downstreamConsecutiveLegsResolver;
    this.availableStates = consecutiveLegs.stream().map(ConsecutiveLegsState::new).collect(Collectors.toList());
    this.availableTransitions = availableStates.stream().collect(Collectors.toMap(ConsecutiveLegsState::consecutiveLegs, this::transitionTo));
  }

  public NavigableMap<ConformablePoint, ConsecutiveLegs> resolveRoute(List<? extends ConformablePoint> conformablePoints) {
    DynamicProgrammer<ConformablePoint, ConsecutiveLegsState> dynamicProgrammer =
        new DynamicProgrammer<>(conformablePoints, availableStates, DynamicProgrammer.Optimization.MAXIMIZE);
    return Maps.transformValues(dynamicProgrammer.optimalPath(), scoredState -> scoredState.state().consecutiveLegs());
  }

  /**
   * Internal wrapper class for state associated with a given configured {@link ConsecutiveLegs} object. Implements dynamic programmer
   * state interface and allows access to the {@link DownstreamConsecutiveLegsResolver} which gives the set of candidate transitions
   * from any given {@link ConsecutiveLegsState}.
   */
  class ConsecutiveLegsState implements DynamicProgrammerState<ConformablePoint> {

    private ConsecutiveLegs consecutiveLegs;

    public ConsecutiveLegsState(ConsecutiveLegs consecutiveLegs) {
      this.consecutiveLegs = consecutiveLegs;
    }

    public ConsecutiveLegs consecutiveLegs() {
      return consecutiveLegs;
    }

    @Override
    public double getValue(ConformablePoint stage) {
      return consecutiveLegs.scorer().score(stage).orElse(0.0);
    }

    /**
     * The set of all possible transitions from the given state - this is taken to be all downstream legs as well as the current leg.
     */
    @Override
    public List<DynamicProgrammerTransition<ConformablePoint, ConsecutiveLegsState>> getPossibleTransitions(ConformablePoint stage) {
      // include the downstream legs as well as the current leg as valid transition targets
      List<ConsecutiveLegs> downstreamLegs = downstreamConsecutiveLegsResolver.downstreamLegsOf(consecutiveLegs);
      if (downstreamLegs.isEmpty()) {
        // if no downstream legs we've reached the end of an element - this means we should zip back to other options and open up transitions
        availableStates.forEach(state -> downstreamLegs.add(state.consecutiveLegs()));
      }

      return Stream.concat(downstreamLegs.stream(), Stream.of(consecutiveLegs()))
          .distinct()
          .map(availableTransitions::get)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
  }

  /**
   * Returns a new anonymous instance of a {@link DynamicProgrammerTransition} for use in the {@link ScoreBasedRouteResolver}.
   */
  private DynamicProgrammerTransition<ConformablePoint, ConsecutiveLegsState> transitionTo(ConsecutiveLegsState consecutiveLegsState) {
    return new DynamicProgrammerTransition<ConformablePoint, ConsecutiveLegsState>() {
      @Override
      public ConsecutiveLegsState getTransition() {
        return consecutiveLegsState;
      }

      @Override
      public Double getTransitionProbability() {
        return 1.0;
      }
    };
  }

  /**
   * Creates a new {@link ScoreBasedRouteResolver} which will assign an input collection of {@link ConformablePoint}s to the provided
   * collection of legs.
   */
  public static ScoreBasedRouteResolver with(List<ConsecutiveLegs> routeLegs) {
    DownstreamConsecutiveLegsResolver downstreamConsecutiveLegsResolver = DownstreamConsecutiveLegsResolver.from(routeLegs);
    return new ScoreBasedRouteResolver(downstreamConsecutiveLegsResolver, routeLegs);
  }
}
