package org.mitre.tdp.boogie.conformance.alg.assign;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CompositeLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Class for generating a {@link LegTransitionGraph} from an input collection of route and a:
 *
 * 1) {@link CombinationStrategy}
 * 2) {@link LinkingStrategy}
 */
public final class TransitionGraphAssembler {

  private final Logger LOG = LoggerFactory.getLogger(TransitionGraphAssembler.class);

  /**
   * See {@link LinkingStrategy}.
   */
  private final LinkingStrategy linkingStrategy;
  /**
   * See {@link CombinationStrategy}.
   */
  private final CombinationStrategy combinationStrategy;

  public TransitionGraphAssembler(
      LinkingStrategy linkingStrategy,
      CombinationStrategy combinationStrategy
  ) {
    this.linkingStrategy = linkingStrategy;
    this.combinationStrategy = combinationStrategy;
  }

  /**
   * Assembles a new {@link LegTransitionGraph} for the given input collection of routes with the provided Combination/Linking
   * strategies.
   */
  public LegTransitionGraph assembleFrom(Collection<? extends Route> routes) {
    Preconditions.checkArgument(!routes.isEmpty(), String.format("Invalid number of supplied routes %s.", routes.size()));
    Preconditions.checkArgument(!routes.stream().allMatch(route -> route.legs().isEmpty()), "At least one route must have legs.");

    Pair<Collection<FlyableLeg>, Collection<Pair<FlyableLeg, FlyableLeg>>> assembled = FlyableLegAssembler.assembleWithLinks(routes);

    Collection<FlyableLeg> flyableLegs = assembled.first();
    LOG.info("Assembled {} total flyable legs from {} input routes.", flyableLegs.size(), routes.size());

    Collection<Pair<FlyableLeg, FlyableLeg>> nativeLinks = assembled.second();
    LOG.info("Assembled {} native links from {} input routes", nativeLinks.size(), routes.size());

    // mapping of each input leg to its composite version
    Map<FlyableLeg, CompositeLeg> legToComposite = combinationStrategy.combineSimilar(flyableLegs);
    LOG.info("Built representative map.");

    Set<CompositeLeg> combined = new HashSet<>(legToComposite.values());
    LOG.info("Combined similar legs down to {} total legs.", combined.size());

    Collection<Pair<FlyableLeg, FlyableLeg>> additionalLinks = linkingStrategy.links(flyableLegs);
    int totalLinks = additionalLinks.size() + nativeLinks.size();
    LOG.info("Generated {} additional links from linking strategy for {} total links.", additionalLinks.size(), totalLinks);

    // mapping of representative (as input into the LegTransitionGraph) to the source CompositeLeg
    Map<FlyableLeg, CompositeLeg> representativeMap = combined.stream()
        .collect(Collectors.toMap(CompositeLeg::representative, Function.identity()));

    LOG.info("Adding {} vertices to flyable leg graph.", combined.size());
    LegTransitionGraph graph = new LegTransitionGraph(representativeMap);
    combined.forEach(union -> graph.addVertex(union.representative()));

    LOG.info("Adding {} edges to flyable leg graph.", totalLinks);
    Consumer<Collection<Pair<FlyableLeg, FlyableLeg>>> linkAdder = col ->
        col.stream()
            .map(pair -> remap(pair, legToComposite::get))
            .filter(pair -> !pair.first().equals(pair.second()))
            .filter(pair -> !graph.containsEdge(pair.second(), pair.first()))
            .forEach(pair -> graph.addEdge(pair.first(), pair.second()));

    linkAdder.accept(nativeLinks);
    linkAdder.accept(additionalLinks);

    LOG.info("Finished assembling transition graph with {} vertices and {} edges", graph.vertexSet().size(), graph.edgeSet().size());
    return graph;
  }

  /**
   * Re-maps the given pair to a new one by applying the provided {@link Function}.
   */
  private Pair<FlyableLeg, FlyableLeg> remap(Pair<FlyableLeg, FlyableLeg> pair, Function<FlyableLeg, CompositeLeg> fn) {
    return Pair.of(
        checkNotNull(fn.apply(pair.first()).representative(), String.format("Failed to re-map: %s", pair.first())),
        checkNotNull(fn.apply(pair.second()).representative(), String.format("Failed to re-map: %s", pair.second()))
    );
  }
}
