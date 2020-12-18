package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Route;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTrellis;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScoringRule;
import org.mitre.tdp.boogie.conformance.alg.assign.score.ScoringStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * The assignment algorithm takes a collection of (potentially filtered) routing information + point information and produces
 * for each point an assigned leg representing the most likely leg the aircraft was flying at that point.
 *
 * This algorithm can be broken up into three primary parts:
 *
 * 1) Leg generation: where input routes are assembled into {@link FlyableLeg}s representing a single leg plus the potentially
 * necessary context of the previous/next leg to determine how it should be flown.
 *
 * 2) Leg combination: where the flyable legs are compared with each other (as logically similar legs may be generated from
 * multiple routes) and reduced into a single final representation. I.e. why have the same TF leg represented twice - the notion
 * of logical equivalence here depends on the supplied {@link CombinationStrategy}.
 *
 * 3) Leg linking: where the previously combined flyable legs are examined to determine whether additional links should be added
 * between them before they are handed off to the {@link FlyableLegGraph} which will back the next stages {@link ViterbiTagger}
 * in terms of determining allowed transitions between leg states.
 *
 * 4) Graph assembly: where the generated links and final combined set of legs are loaded into a {@link FlyableLegGraph} which
 * will back the allowed transitions between leg states within the final stages {@link ViterbiTagger}.
 *
 * 3) Leg assignment: where the points and leg graph are leveraged by the {@link ViterbiTagger} to produce an optimal sequence
 * of leg assignments.
 *
 * These assignments are then returned as a mapping from {@link ConformablePoint} to {@link FlyableLeg}.
 */
public final class AssignmentAlgorithm {

  private static final Logger LOG = LoggerFactory.getLogger(AssignmentAlgorithm.class);

  /**
   * See {@link LinkingStrategy}.
   */
  private final LinkingStrategy linkingStrategy;
  /**
   * See {@link CombinationStrategy}.
   */
  private final CombinationStrategy combinationStrategy;
  /**
   * See {@link ScoringStrategy}.
   */
  private final ScoringStrategy scoringStrategy;

  public AssignmentAlgorithm(
      LinkingStrategy linkingStrategy,
      CombinationStrategy combinationStrategy,
      ScoringStrategy scoringStrategy
  ) {
    this.linkingStrategy = linkingStrategy;
    this.combinationStrategy = combinationStrategy;
    this.scoringStrategy = scoringStrategy;
  }

  /**
   * Returns the resultant {@link ViterbiTrellis} from running the assignment algorithm on the given collection of points and routes.
   */
  public ViterbiTrellis<ConformablePoint, FlyableLeg> trellis(
      List<? extends ConformablePoint> points,
      List<? extends Route> routes) {
    Preconditions.checkArgument(!points.isEmpty(), "Invalid number of supplied points %s.", points.size());
    Preconditions.checkArgument(!routes.isEmpty(), "Invalid number of supplied routes %s.", routes.size());
    Preconditions.checkArgument(!routes.stream().allMatch(route -> route.legs().isEmpty()), "At least one route must have legs.");

    Pair<Collection<FlyableLeg>, Collection<Pair<FlyableLeg, FlyableLeg>>> assembled = FlyableLegAssembler.assembleWithLinks(routes);

    Collection<FlyableLeg> flyableLegs = assembled.first();
    LOG.info("Assembled {} total flyable legs from {} input routes.", flyableLegs.size(), routes.size());

    Collection<Pair<FlyableLeg, FlyableLeg>> nativeLinks = assembled.second();
    LOG.info("Assembled {} native links from {} input routes", nativeLinks.size(), routes.size());

    Map<FlyableLeg, FlyableLeg> representativeMap = combinationStrategy.combineSimilar(flyableLegs);
    LOG.info("Build representative map.");

    Set<FlyableLeg> combined = new HashSet<>(representativeMap.values());
    LOG.info("Combined similar legs down to {} total legs.", combined.size());

    Collection<Pair<FlyableLeg, FlyableLeg>> additionalLinks = linkingStrategy.links(flyableLegs);
    int totalLinks = additionalLinks.size() + nativeLinks.size();
    LOG.info("Generated {} additional links from linking strategy for {} total links.", additionalLinks.size(), totalLinks);

    LOG.info("Adding {} vertices to flyable leg graph.", combined.size());
    FlyableLegGraph graph = new FlyableLegGraph();
    combined.forEach(graph::addVertex);

    LOG.info("Adding {} edges to flyable leg graph.", totalLinks);
    nativeLinks.stream().map(pair -> remap(pair, representativeMap::get)).forEach(pair -> graph.addEdge(pair.first(), pair.second()));
    additionalLinks.stream().map(pair -> remap(pair, representativeMap::get)).forEach(pair -> graph.addEdge(pair.first(), pair.second()));

    LOG.info("Generating ViterbiTagger instance.");
    ViterbiTagger<ConformablePoint, FlyableLeg> tagger = new ViterbiTagger<>(
        points,
        flyableLegs,
        this::legScore,
        transitionFunction(graph),
        this::transitionScore);

    LOG.info("Generating trellis.");
    return tagger.trellis();
  }

  /**
   * Returns a sorted mapping of {@link ConformablePoint} to associated {@link FlyableLeg} as derived via the {@link ViterbiTagger}.
   */
  public NavigableMap<ConformablePoint, FlyableLeg> assignments(
      List<? extends ConformablePoint> points,
      List<? extends Route> routes) {
    ViterbiTrellis<ConformablePoint, FlyableLeg> trellis = trellis(points, routes);

    LOG.info("Computing optimal path.");
    return trellis.optimalPath();
  }

  /**
   * Re-maps the given pair to a new one by applying the provided {@link Function}.
   */
  private Pair<FlyableLeg, FlyableLeg> remap(Pair<FlyableLeg, FlyableLeg> pair, Function<FlyableLeg, FlyableLeg> fn) {
    return Pair.of(fn.apply(pair.first()), fn.apply(pair.second()));
  }

  private Double legScore(ConformablePoint pt, FlyableLeg leg) {
    OnLegScorer scorer = scoringStrategy.onLegScorerFor(leg);
    return scorer.score(pt, leg).orElseThrow(() -> new IllegalStateException("No leg score provided for flyable leg ".concat(leg.toString())));
  }

  private Function<FlyableLeg, Collection<FlyableLeg>> transitionFunction(FlyableLegGraph graph) {
    return flyableLeg -> Stream.concat(graph.downstreamLegsOf(flyableLeg).stream(), Stream.of(flyableLeg)).collect(Collectors.toList());
  }

  private Double transitionScore(FlyableLeg l1, FlyableLeg l2) {
    return scoringStrategy.legTransitionScorer(l1).transitionScore(l1, l2).orElseThrow(() -> new IllegalStateException("No LegTransitionScorer provided for flyable leg ".concat(l1.toString())));
  }
}
