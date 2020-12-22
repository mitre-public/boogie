package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTrellis;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.ScoringStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The route assigner takes a collection of (potentially filtered) routing information + point information and produces for
 * each point an assigned leg representing the most likely leg the aircraft was flying at that point.
 *
 * This algorithm can be broken up into five primary parts:
 *
 * 1) Leg generation: where input routes are assembled into {@link FlyableLeg}s representing a single leg plus the potentially
 * necessary context of the previous/next leg to determine how it should be flown.
 *
 * 2) Leg combination: where the flyable legs are compared with each other (as logically similar legs may be generated from
 * multiple routes) and reduced into a single final representation. I.e. why have the same TF leg represented twice - the notion
 * of logical equivalence here depends on the supplied {@link CombinationStrategy}.
 *
 * 3) Leg linking: where the previously combined flyable legs are examined to determine whether additional links should be added
 * between them before they are handed off to the {@link LegTransitionGraph} which will back the next stages {@link ViterbiTagger}
 * in terms of determining allowed transitions between leg states.
 *
 * 4) Graph assembly: where the generated links and final combined set of legs are loaded into a {@link LegTransitionGraph} which
 * will back the allowed transitions between leg states within the final stages {@link ViterbiTagger}.
 *
 * 5) Leg assignment: where the points and leg graph are leveraged by the {@link ViterbiTagger} to produce an optimal sequence
 * of leg assignments.
 *
 * These assignments are then returned as a mapping from {@link ConformablePoint} to {@link FlyableLeg}.
 *
 * Note - all of steps 1-4 are handled in the delegate class {@link TransitionGraphAssembler} which can (given a combination and
 * linking strategy) be used to generated a {@link LegTransitionGraph}.
 */
public final class RouteAssigner {

  private static final Logger LOG = LoggerFactory.getLogger(RouteAssigner.class);

  /**
   * See {@link TransitionGraphAssembler}
   */
  private final TransitionGraphAssembler transitionGraphAssembler;
  /**
   * See {@link ScoringStrategy}.
   */
  private final ScoringStrategy scoringStrategy;

  public RouteAssigner(
      LinkingStrategy linkingStrategy,
      CombinationStrategy combinationStrategy,
      ScoringStrategy scoringStrategy
  ) {
    this.transitionGraphAssembler = new TransitionGraphAssembler(linkingStrategy, combinationStrategy);
    this.scoringStrategy = scoringStrategy;
  }

  /**
   * Returns the resultant {@link ViterbiTrellis} from running the assignment algorithm on the given collection of points and routes.
   */
  public ViterbiTagger<ConformablePoint, FlyableLeg> tagger(
      Collection<? extends ConformablePoint> points,
      Collection<? extends Route> routes) {
    Preconditions.checkArgument(!points.isEmpty(), String.format("Invalid number of supplied points %s.", points.size()));

    LOG.info("Generating transition graph.");
    LegTransitionGraph graph = transitionGraphAssembler.assembleFrom(routes);

    LOG.info("Generating ViterbiTagger instance.");
    return new ViterbiTagger<>(
        points,
        graph.vertexSet(),
        this::legScore,
        transitionFunction(graph),
        this::transitionScore);
  }

  public ViterbiTrellis<ConformablePoint, FlyableLeg> trellis(
      Collection<? extends ConformablePoint> points,
      Collection<? extends Route> routes) {
    ViterbiTagger<ConformablePoint, FlyableLeg> tagger = tagger(points, routes);

    LOG.info("Generating trellis.");
    return tagger.trellis();
  }

  /**
   * Returns a sorted mapping of {@link ConformablePoint} to associated {@link FlyableLeg} as derived via the {@link ViterbiTagger}.
   */
  public NavigableMap<ConformablePoint, FlyableLeg> assignments(
      Collection<? extends ConformablePoint> points,
      Collection<? extends Route> routes) {
    ViterbiTrellis<ConformablePoint, FlyableLeg> trellis = trellis(points, routes);

    LOG.info("Computing optimal path.");
    return trellis.optimalPath();
  }

  private Double legScore(ConformablePoint pt, FlyableLeg leg) {
    OnLegScorer scorer = scoringStrategy.onLegScorerFor(leg);
    return scorer.score(pt, leg).orElseThrow(() -> new IllegalStateException("No leg score provided for flyable leg ".concat(leg.toString())));
  }

  private Function<FlyableLeg, Collection<FlyableLeg>> transitionFunction(LegTransitionGraph graph) {
    return flyableLeg -> Stream.concat(graph.downstreamLegsOf(flyableLeg).stream(), Stream.of(flyableLeg)).collect(Collectors.toList());
  }

  private Double transitionScore(FlyableLeg l1, FlyableLeg l2) {
    return scoringStrategy.legTransitionScorer(l1).transitionScore(l1, l2).orElseThrow(() -> new IllegalStateException("No LegTransitionScorer provided for flyable leg ".concat(l1.toString())));
  }
}
