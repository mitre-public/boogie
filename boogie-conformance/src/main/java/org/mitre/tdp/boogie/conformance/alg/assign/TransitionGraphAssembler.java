package org.mitre.tdp.boogie.conformance.alg.assign;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CompositeLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.link.SuppliedLinkStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Class for generating a {@link LegTransitionGraph} from an input collection of routes and a:
 *
 * 1) {@link CombinationStrategy}
 * 2) {@link LinkingStrategy}
 * 3) {@link BiFunction} - to generate edge weights between {@link FlyableLeg} vertices.
 *
 * At a high level this class takes a collection of {@link Route} records representing naturally linked legs (e.g. member legs of
 * the same transition, legs filed consecutively in a flight plan) and a separate "linking strategy" which can be used to supply
 * additional edges (e.g. {@link SuppliedLinkStrategy}) and generates a graph representing all of the legs how flights transition
 * to/from them.
 *
 * Note - this assembler automatically adds self-edges/loops under the assumption that the aircraft can always stay on its current
 * leg (though the weight of these edges are still configurable via the provided {@link #transitionScorer}).
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
  /**
   * A simple {@link BiFunction} for scoring transitions between {@link FlyableLeg}s - this is used internally to set the edge
   * weights between vertices in the transition graph.
   */
  private final BiFunction<FlyableLeg, FlyableLeg, Double> transitionScorer;

  public TransitionGraphAssembler(
      LinkingStrategy linkingStrategy,
      CombinationStrategy combinationStrategy,
      BiFunction<FlyableLeg, FlyableLeg, Double> transitionScorer
  ) {
    this.linkingStrategy = requireNonNull(linkingStrategy);
    this.combinationStrategy = requireNonNull(combinationStrategy);
    this.transitionScorer = requireNonNull(transitionScorer);
  }

  /**
   * Assembles a new {@link LegTransitionGraph} for the given input collection of routes with the provided Combination/Linking
   * strategies.
   */
  public AssemblyResult assembleFrom(Collection<? extends Route> routes) {
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
    Graph<FlyableLeg, DefaultWeightedEdge> graph = initializeGraph();
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

    // add the remaining self-links and then modify the underlying edge weights in line with the transition scorer
    LOG.info("Adding {} self-edges.", graph.vertexSet().size());
    graph.vertexSet().forEach(vertex -> graph.addEdge(vertex, vertex));

    LOG.info("Updating edge weights in line with the strategy.");
    graph.edgeSet().forEach(edge -> {
      FlyableLeg src = graph.getEdgeSource(edge);
      FlyableLeg tgt = graph.getEdgeTarget(edge);

      double score = transitionScorer.apply(src, tgt);
      graph.setEdgeWeight(edge, score);
    });

    LOG.info("Finished assembling transition graph with {} vertices and {} edges", graph.vertexSet().size(), graph.edgeSet().size());
    return new AssemblyResult(graph, representativeMap);
  }

  /**
   * Initializes a new graph to hold valid transitions between legs which:
   *
   * Directed
   * Weighted
   * Allows Self-Loops
   */
  private Graph<FlyableLeg, DefaultWeightedEdge> initializeGraph() {
    return GraphTypeBuilder.<FlyableLeg, DefaultWeightedEdge>directed()
        .edgeClass(DefaultWeightedEdge.class)
        .allowingSelfLoops(true)
        .weighted(true)
        .buildGraph();
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

  /**
   * Thin wrapper class for the result of the {@link TransitionGraphAssembler} containing both the assembled graph and the
   * lookup table mirroring the reduction performed by the {@link CombinationStrategy}.
   */
  public static final class AssemblyResult {

    private final Graph<FlyableLeg, DefaultWeightedEdge> graph;
    private final Map<FlyableLeg, CompositeLeg> representativeMap;

    public AssemblyResult(
        Graph<FlyableLeg, DefaultWeightedEdge> graph,
        Map<FlyableLeg, CompositeLeg> representativeMap
    ) {
      this.graph = requireNonNull(graph);
      this.representativeMap = requireNonNull(representativeMap);
    }

    public Graph<FlyableLeg, DefaultWeightedEdge> graph() {
      return graph;
    }

    public Map<FlyableLeg, CompositeLeg> representativeMap() {
      return representativeMap;
    }
  }
}
