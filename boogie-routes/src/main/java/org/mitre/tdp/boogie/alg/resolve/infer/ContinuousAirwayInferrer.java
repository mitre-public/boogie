package org.mitre.tdp.boogie.alg.resolve.infer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * Infers additional airway segments between a multiply-resolved airway section and a downstream fix.
 * <p>
 * A single airway identifier in a route string (e.g. {@code FIX.W1.FIX}) may resolve to several {@link Airway} segments with
 * the same name. Source data can split one logical route into multiple records (sequence-number jumps, boundary codes, etc.). When
 * those segments connect end to end — the last fix of one equals the first fix of the next — they may represent one continuous
 * path to the filed exit fix.
 * <p>
 * In practice a single airway identifier resolves to a small number of segments. In {@code A424-22std.dat.gz} the maximum is
 * twelve assembled segments sharing one identifier ({@code V16}); most identifiers produce one or two. The search is tuned for
 * that scale rather than large graphs.
 *
 * @see SectionInferrer
 * @see ResolvedToken#standardAirway(Airway)
 */
final class ContinuousAirwayInferrer implements SectionInferrer {

  ContinuousAirwayInferrer(){}

  /**
   * Returns the fix identifier filed in the right section, if present.
   */
  private static Optional<String> rightFixIdent(ResolvedTokens right) {
    return right.resolvedTokens().stream()
        .flatMap(t -> ResolvedTokenVisitor.fix(t).stream())
        .findFirst()
        .map(Fix::fixIdentifier);
  }

  /**
   * Collects all airway candidates resolved for the left section.
   */
  private static List<Airway> leftAirways(ResolvedTokens left) {
    return left.resolvedTokens().stream()
        .flatMap(t -> ResolvedTokenVisitor.airway(t).stream())
        .toList();
  }

  /**
   * Converts each continuation layer into a {@link ResolvedTokens} section sharing the original route token.
   */
  private static List<ResolvedTokens> inferContinuations(RouteToken routeToken, List<Airway> airways, String targetFixIdent) {
    return AirwayGraph.of(airways)
        .continuationLayers(targetFixIdent)
        .stream()
        .map(continuations -> new ResolvedTokens(
            routeToken,
            continuations.stream().<ResolvedToken>map(ResolvedToken::standardAirway).toList()))
        .toList();
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {
    return Optional.of(left)
        .filter(section -> section.resolvedTokens().size() > 1)
        .flatMap(section -> rightFixIdent(right)
            .map(target -> inferContinuations(section.routeToken(), leftAirways(section), target)))
        .orElseGet(List::of);
  }

  /**
   * First and last fix identifiers for an airway segment.
   */
  private record AirwayEndpoints(String firstFix, String lastFix) {
    private static AirwayEndpoints of(Airway airway) {
      return new AirwayEndpoints(
          airway.legs().get(0).associatedFix().orElseThrow().fixIdentifier(),
          airway.legs().get(airway.legs().size() - 1).associatedFix().orElseThrow().fixIdentifier());
    }
  }

  /**
   * An airway segment with a stable index into the left-section candidate list.
   */
  private record IndexedAirway(int index, Airway airway, AirwayEndpoints endpoints) {
    private static IndexedAirway of(int index, Airway airway) {
      return new IndexedAirway(index, airway, AirwayEndpoints.of(airway));
    }

    private String firstFix() {
      return endpoints.firstFix();
    }

    private String lastFix() {
      return endpoints.lastFix();
    }
  }

  /**
   * Directed graph of airway segments linked by matching fix identifiers.
   *
   * <p>An edge exists from segment {@code A} to segment {@code B} when {@code A}'s last fix equals {@code B}'s first fix.
   * {@link #successorsByFirstFix} is indexed by first fix so successors of a segment are found via its last fix.
   */
  private record AirwayGraph(List<IndexedAirway> indexed, Map<String, List<Integer>> successorsByFirstFix) {

    private static AirwayGraph of(List<Airway> airways) {
      if (airways.isEmpty()) {
        return new AirwayGraph(List.of(), Map.of());
      }

      List<IndexedAirway> indexed = IntStream.range(0, airways.size())
          .mapToObj(i -> IndexedAirway.of(i, airways.get(i)))
          .toList();

      Map<String, List<Integer>> successorsByFirstFix = indexed.stream()
          .collect(Collectors.groupingBy(
              IndexedAirway::firstFix,
              Collectors.mapping(IndexedAirway::index, Collectors.toList())));

      return new AirwayGraph(indexed, successorsByFirstFix);
    }

    /**
     * Returns ordered layers of continuation segments that can appear between the left airway section and the target fix.
     *
     * <p>Layer {@code 0} holds first-hop continuations, layer {@code 1} holds second-hop continuations, and so on. Each layer may
     * contain multiple {@link Airway} alternatives when several valid chains share or diverge at that hop.
     */
    private List<Set<Airway>> continuationLayers(String targetFixIdent) {
      if (indexed.isEmpty()) {
        return List.of();
      }

      List<Airway> airways = indexed.stream().map(IndexedAirway::airway).toList();
      return SearchContext.begin(airways)
          .exploreAllFrom(this, startingIndices(targetFixIdent), targetFixIdent)
          .toLayers();
    }

    /**
     * Indices of segments that can begin a path (those not already ending at the target fix).
     */
    private IntStream startingIndices(String targetFixIdent) {
      return indexed.stream()
          .mapToInt(IndexedAirway::index)
          .filter(start -> !endsAt(start, targetFixIdent));
    }

    private boolean endsAt(int index, String targetFixIdent) {
      return indexed.get(index).lastFix().equals(targetFixIdent);
    }

    /**
     * Indices of segments whose first fix matches the last fix of the segment at {@code index}.
     */
    private List<Integer> successorsOf(int index) {
      return successorsByFirstFix.getOrDefault(indexed.get(index).lastFix(), List.of());
    }
  }

  /**
   * Mutable scratch-space for backtracking search over {@link AirwayGraph}.
   *
   * <p>{@link #pathLength} advances immutably on {@link #enter} and {@link #leave}; {@link #onPath} and {@link #path} are reused
   * to avoid allocating on every recursive step. Completed paths are folded into {@link #layers} when a segment ending at the
   * target fix is reached.
   */
  private record SearchContext(List<Airway> airways, boolean[] onPath, int[] path, int pathLength, List<LinkedHashSet<Airway>> layers) {

    private static SearchContext begin(List<Airway> airways) {
      return new SearchContext(airways, new boolean[airways.size()], new int[airways.size()], 0, new ArrayList<>());
    }

    /**
     * Explores from each candidate start index and accumulates all valid continuation layers.
     */
    private SearchContext exploreAllFrom(AirwayGraph graph, IntStream starts, String targetFixIdent) {
      return starts.boxed()
          .reduce(this, (context, start) -> context.enter(start).exploreFrom(graph, start, targetFixIdent).leave(start), (left, right) -> right);
    }

    /**
     * Depth-first search for paths from {@code current} to the target fix, avoiding cycles via {@link #onPath}.
     */
    private SearchContext exploreFrom(AirwayGraph graph, int current, String targetFixIdent) {
      if (graph.endsAt(current, targetFixIdent)) {
        return absorbCurrentPath();
      }

      return graph.successorsOf(current).stream()
          .filter(next -> !onPath[next])
          .reduce(this, (context, next) -> context.enter(next).exploreFrom(graph, next, targetFixIdent).leave(next), (left, right) -> right);
    }

    private SearchContext enter(int index) {
      onPath[index] = true;
      path[pathLength] = index;
      return new SearchContext(airways, onPath, path, pathLength + 1, layers);
    }

    private SearchContext leave(int index) {
      onPath[index] = false;
      return new SearchContext(airways, onPath, path, pathLength - 1, layers);
    }

    /**
     * Adds all segments after the path start into the appropriate continuation layers.
     */
    private SearchContext absorbCurrentPath() {
      IntStream.range(1, pathLength).forEach(index -> addToLayer(index - 1, airways.get(path[index])));
      return this;
    }

    private void addToLayer(int layer, Airway airway) {
      while (layers.size() <= layer) {
        layers.add(new LinkedHashSet<>());
      }
      layers.get(layer).add(airway);
    }

    private List<Set<Airway>> toLayers() {
      return List.copyOf(layers);
    }
  }
}
