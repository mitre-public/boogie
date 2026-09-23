package org.mitre.tdp.boogie.alg.chooser;

import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Airways;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Compares repeated endpoint searches with the production chooser's per-source reuse on an actual airway route graph.
 * The six UG521 legs are candidate entries; one, two, or all seven UM219 legs are candidate exits.
 * Graph construction and verification of every candidate path run outside timing.
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgsAppend = {"-Xms512m", "-Xmx512m"})
@Threads(1)
public class ShortestPathBenchmark {

  @Param({"1", "2", "7"})
  public int exitCount;

  private DijkstraShortestPath<Leg, DefaultWeightedEdge> algorithm;
  private Set<Leg> entries;
  private Set<Leg> exits;

  @Setup(Level.Trial)
  public void setup() {
    Airway ug521 = Airways.UG521();
    Airway ub881 = Airways.UB881();
    Airway um219 = Airways.UM219();
    List<ResolvedTokens> resolved = List.of(
        resolvedAirway(ug521, 0),
        resolvedAirway(ub881, 1),
        resolvedAirway(um219, 2)
    );
    GraphicalRouteChooser chooser = new GraphicalRouteChooser(TokenMapper.standard());
    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph = chooser.constructRouteGraph(chooser.toLinkableTokens(resolved));
    entries = verticesFor(ug521, graph.vertexSet());
    Set<Leg> allExits = verticesFor(um219, graph.vertexSet());
    exits = allExits.stream().limit(exitCount).collect(Collectors.toCollection(LinkedHashSet::new));
    if (entries.size() != 6 || allExits.size() != 7 || exits.size() != exitCount) {
      throw new IllegalStateException("Expected six UG521 entries and " + exitCount + " of seven UM219 exits.");
    }
    algorithm = new DijkstraShortestPath<>(graph);

    List<GraphPath<Leg, DefaultWeightedEdge>> expected = repeatedCandidates().toList();
    List<GraphPath<Leg, DefaultWeightedEdge>> actual = GraphicalRouteChooser.candidatePaths(algorithm, entries, exits).toList();
    if (expected.size() != entries.size() * exits.size() || expected.size() != actual.size()) {
      throw new IllegalStateException("Both implementations must enumerate every entry/exit pair.");
    }
    for (int index = 0; index < expected.size(); index++) {
      GraphPath<Leg, DefaultWeightedEdge> before = expected.get(index);
      GraphPath<Leg, DefaultWeightedEdge> after = actual.get(index);
      if (before == null || after == null
          || !before.getVertexList().equals(after.getVertexList())
          || Double.compare(before.getWeight(), after.getWeight()) != 0) {
        throw new IllegalStateException("Candidate " + index + " must have the same reachable path and weight.");
      }
    }
  }

  @Benchmark
  public GraphPath<Leg, DefaultWeightedEdge> repeatedSearches() {
    return shortest(repeatedCandidates());
  }

  @Benchmark
  public GraphPath<Leg, DefaultWeightedEdge> reusedSearches() {
    return shortest(GraphicalRouteChooser.candidatePaths(algorithm, entries, exits));
  }

  private Stream<GraphPath<Leg, DefaultWeightedEdge>> repeatedCandidates() {
    return cartesianProduct(entries, exits).stream()
        .map(pair -> algorithm.getPath(pair.first(), pair.second()));
  }

  private static GraphPath<Leg, DefaultWeightedEdge> shortest(Stream<GraphPath<Leg, DefaultWeightedEdge>> paths) {
    return paths.filter(Objects::nonNull).min(Comparator.comparing(GraphPath::getWeight)).orElseThrow();
  }

  private static ResolvedTokens resolvedAirway(Airway airway, int index) {
    return new ResolvedTokens(RouteToken.standard(airway.airwayIdentifier(), index), List.of(ResolvedToken.standardAirway(airway)));
  }

  private static Set<Leg> verticesFor(Airway airway, Set<Leg> graphVertices) {
    Set<Leg> originalLegs = Collections.newSetFromMap(new IdentityHashMap<>());
    originalLegs.addAll(airway.legs());
    Set<Leg> selected = new LinkedHashSet<>();
    for (Leg vertex : graphVertices) {
      // Chooser vertices delegate to the original leg through the public visitor API.
      // Identity keeps equal fixes on adjacent airway tokens separate and preserves graph encounter order.
      vertex.accept(new Leg.Visitor() {
        @Override
        public void visit(Leg.Standard standard) {
          include(standard);
        }

        @Override
        public void visit(Leg.Record<?> record) {
          include(record);
        }

        private void include(Leg original) {
          if (originalLegs.contains(original)) {
            selected.add(vertex);
          }
        }
      });
    }
    return selected;
  }
}
