package org.mitre.tdp.boogie.conformance.alg.assign;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.mitre.tdp.boogie.viterbi.GraphBackedViterbiTransitionStrategy;
import org.mitre.tdp.boogie.viterbi.ViterbiScoringStrategy;
import org.mitre.tdp.boogie.viterbi.ViterbiTagger;
import org.mitre.tdp.boogie.viterbi.ViterbiTrellis;
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
   * See {@link ViterbiScoringStrategy}.
   */
  private final ViterbiScoringStrategy<ConformablePoint, FlyableLeg> scoringStrategy;

  public RouteAssigner(
      LinkingStrategy linkingStrategy,
      CombinationStrategy combinationStrategy,
      ViterbiScoringStrategy<ConformablePoint, FlyableLeg> scoringStrategy,
      BiFunction<FlyableLeg, FlyableLeg, Double> transitionScorer
  ) {
    this.transitionGraphAssembler = new TransitionGraphAssembler(linkingStrategy, combinationStrategy, transitionScorer);
    this.scoringStrategy = requireNonNull(scoringStrategy);
  }

  public TransitionGraphAssembler.AssemblyResult transitionGraph(Collection<? extends Route> routes) {
    LOG.info("Generating transition graph.");
    return transitionGraphAssembler.assembleFrom(routes);
  }

  public ViterbiTagger<ConformablePoint, FlyableLeg> tagger(
      Collection<? extends ConformablePoint> points,
      Collection<? extends Route> routes) {
    return tagger(points, transitionGraph(routes).graph());
  }

  public ViterbiTagger<ConformablePoint, FlyableLeg> tagger(
      Collection<? extends ConformablePoint> points,
      Graph<FlyableLeg, DefaultWeightedEdge> transitionGraph) {
    Preconditions.checkArgument(!points.isEmpty(), String.format("Invalid number of supplied points %s.", points.size()));

    LOG.info("Generating ViterbiTagger instance.");
    return ViterbiTagger.with(
        points,
        transitionGraph.vertexSet(),
        scoringStrategy,
        new GraphBackedViterbiTransitionStrategy<>(transitionGraph)
    );
  }

  public ViterbiTrellis<ConformablePoint, FlyableLeg> trellis(
      Collection<? extends ConformablePoint> points,
      Graph<FlyableLeg, DefaultWeightedEdge> transitionGraph) {
    ViterbiTagger<ConformablePoint, FlyableLeg> tagger = tagger(points, transitionGraph);

    LOG.info("Generating trellis.");
    return tagger.trellis();
  }

  public ViterbiTrellis<ConformablePoint, FlyableLeg> trellis(
      Collection<? extends ConformablePoint> points,
      Collection<? extends Route> routes) {
    return trellis(points, transitionGraph(routes).graph());
  }

  public Map<ConformablePoint, FlyableLeg> assignments(
      Collection<? extends ConformablePoint> points,
      Graph<FlyableLeg, DefaultWeightedEdge> transitionGraph) {
    ViterbiTrellis<ConformablePoint, FlyableLeg> trellis = trellis(points, transitionGraph);

    LOG.info("Computing optimal path.");
    return trellis.optimalPath();
  }

  /**
   * Returns a sorted mapping of {@link ConformablePoint} to associated {@link FlyableLeg} as derived via the {@link ViterbiTagger}.
   */
  public Map<ConformablePoint, FlyableLeg> assignments(
      Collection<? extends ConformablePoint> points,
      Collection<? extends Route> routes) {
    return assignments(points, transitionGraph(routes).graph());
  }
}
