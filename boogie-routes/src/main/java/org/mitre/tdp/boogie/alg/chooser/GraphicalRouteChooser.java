package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;
import static org.mitre.tdp.boogie.util.Iterators.openClose2;

import java.time.Duration;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkableToken;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkedLegs;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenGrapher;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;

final class GraphicalRouteChooser implements RouteChooser {

  private static final Logger LOG = LoggerFactory.getLogger(GraphicalRouteChooser.class);

  private final TokenMapper mapper;

  GraphicalRouteChooser(TokenMapper mapper) {
    this.mapper = requireNonNull(mapper);
  }

  @Override
  public List<ResolvedLeg> chooseRoute(List<ResolvedTokens> resolvedTokens) {

    List<LinkableTokens> linkableTokens = toLinkableTokens(resolvedTokens);

    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> routeGraph = constructRouteGraph(linkableTokens);
    ifTraceEnabled(l -> l.trace("Constructed the following graph:\n {}.", GraphExporter.INSTANCE.apply(routeGraph)));

    Set<Leg> resolvedEntryPoints = logResolvedPoints("entry", resolveEntryPoints(linkableTokens));
    Set<Leg> resolvedExitPoints = logResolvedPoints("exit", resolveExitPoints(linkableTokens));

    DijkstraShortestPath<Leg, DefaultWeightedEdge> shortestPathAlgorithm = new DijkstraShortestPath<>(routeGraph);

    ifDebugEnabled(l -> l.debug("- Identifying shortest path: {}", resolvedEntryPoints.size() * resolvedExitPoints.size()));
    ifDebugEnabled(l -> l.debug(String.format("  %10s %10s %10s %30s", "Start", "End", "Length", "Weight")));

    List<LinkableLeg> shortestPath = cartesianProduct(resolvedEntryPoints, resolvedExitPoints).stream()
        .map(pair -> shortestPathAlgorithm.getPath(pair.first(), pair.second()))
        .filter(Objects::nonNull)
        .peek(this::logPathCandidate)
        .min(Comparator.comparing(GraphPath::getWeight))
        .map(GraphPath::getVertexList)
        .stream()
        .flatMap(Collection::stream)
        .map(LinkableLeg.class::cast)
        .collect(toList());

    Map<LinkableLeg, LinkableLeg> fixedUp = fixupInventedLegs(shortestPath);
    return shortestPath.stream().map(l -> fixedUp.getOrDefault(l, l)).map(this::makeResolvedLeg).collect(toList());
  }

  List<LinkableTokens> toLinkableTokens(List<ResolvedTokens> resolvedTokens) {
    return resolvedTokens.stream().map(this::make).collect(toList());
  }

  SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> constructRouteGraph(List<LinkableTokens> linkableTokens) {

    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    linkableTokens.stream().flatMap(tokens -> tokens.graphRepresentation().stream())
        .forEach(linkedLegs -> addLinkedLegTo(graph, linkedLegs));

    openClose2(
        linkableTokens,
        LinkableTokens::nonEmpty,
        (l, h) -> h.nonEmpty(),
        (previous, next, skip) -> previous.links(next).forEach(linkedLegs -> addLinkedLegTo(graph, linkedLegs))
    );

    return graph;
  }

  /**
   * Occasionally as part of the linking process new legs, not directly part of a {@link ResolvedToken}'s {@link TokenGrapher}
   * representation, may be "invented" on the fly (see {@code Any->Approach} linking).
   *
   * <p>When this happens the invented on-the-fly leg won't be associated with any {@link ResolvedToken}, this method exists to
   * propagate the resolved token of the last non-invented element into the invented leg.
   *
   * <p>In the context of approaches this means the glue leg between say a STAR and the approach will be associated with the token
   * from the STAR (as opposed to the Approach).
   */
  private Map<LinkableLeg, LinkableLeg> fixupInventedLegs(List<LinkableLeg> linkableLegs) {

    Map<LinkableLeg, LinkableLeg> fixedUp = new HashMap<>();

    // tolerant of never hitting the close criteria
    openClose2(
        linkableLegs,
        l -> l.resolvedToken().isPresent(),
        (l, h) -> h.resolvedToken().isPresent(),
        (p, n, invented) -> invented.forEach(l -> fixedUp.put(l, fixupInventedLeg(p, l, n)))
    );

    return fixedUp;
  }

  private LinkableLeg fixupInventedLeg(LinkableLeg previous, LinkableLeg invented, LinkableLeg next) {
    return next.toBuilder().leg(invented).build();
  }

  private ResolvedLeg makeResolvedLeg(LinkableLeg leg) {
    return ResolvedLeg.create(leg.routeToken().orElseThrow(), leg.resolvedToken().orElseThrow(), leg.leg());
  }

  private void addLinkedLegTo(SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph, LinkedLegs link) {
    graph.addVertex(link.source());
    graph.addVertex(link.target());

    // :loops: - annoying to filter these out upstream and easy enough to skip here
    // if the edge already exists return is null - and we don't want to override initial value of edge
    if (!link.source().equals(link.target()) && !graph.containsEdge(link.source(), link.target())) {
      DefaultWeightedEdge edge = graph.addEdge(link.source(), link.target());
      graph.setEdgeWeight(edge, link.linkWeight());
    }
  }

  /**
   * Resolves the target entry points from the resolved set of sections to be the source legs of the linked legs in the first
   * resolved section of the route string.
   *
   * <p>Generally speaking the returned collection will be a singleton leg representing the departure airport - but for cases where
   * we may have malformed route strings this formulation should remain robust.
   */
  private LinkedHashSet<Leg> resolveEntryPoints(List<LinkableTokens> linkableTokens) {
    return linkableTokens.stream()
        // drop sections without expanded legs (or with no source elements to expand)
        .filter(LinkableTokens::nonEmpty)
        .findFirst()
        .stream()
        .flatMap(tokens -> tokens.graphRepresentation().stream())
        .map(LinkedLegs::source)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * Resolves the target exit points from the resolved set of sections to be the target legs of the linked legs in the last
   * resolved section of the route string.
   *
   * <p>Generally speaking the returned collection will be a singleton leg representing the arrival airport - but for cases where
   * we may have malformed route strings this formulation should remain robust.
   */
  private LinkedHashSet<Leg> resolveExitPoints(List<LinkableTokens> linkableTokens) {
    return linkableTokens.stream()
        // drop sections without expanded legs (or with no source elements to expand)
        .filter(LinkableTokens::nonEmpty)
        .reduce((l1, l2) -> l2)
        .stream()
        .flatMap(tokens -> tokens.graphRepresentation().stream())
        .map(LinkedLegs::source)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * Converts the incoming {@link ResolvedTokens} to a collection object containing their linkable form: {@link LinkableTokens}
   * which wraps {@link LinkedLegs} source/target legs in identity information based on the route/resolved tokens they came from.
   *
   * <p>This identity does two things:
   * <ol>
   *   <li>It preserves the identity of equivalent legs (by equals/hashCode) sourced from different tokens so we can reconstruct
   *   which route/resolved token a leg came from even if its "identical" from a content perspective</li>
   *   <li>It serves as a convenient way to construct the outgoing {@link ResolvedLeg}s expected from a chooser implementation</li>
   * </ol>
   */
  private LinkableTokens make(ResolvedTokens resolvedTokens) {

    LinkedHashMap<LinkableToken, ResolvedToken> tokenMap = resolvedTokens.resolvedTokens().stream()
        .collect(toMap(mapper::map, Function.identity()));

    LinkedHashMap<Leg, LinkableLeg> legMap = tokenMap.keySet().stream()
        .flatMap(token -> token.graphRepresentation().stream()
            .flatMap(linkedLegs -> Stream.of(linkedLegs.source(), linkedLegs.target()))
            .distinct()
            .map(leg -> Pair.of(
                leg,
                LinkableLeg.builder()
                    .routeToken(resolvedTokens.routeToken())
                    .resolvedToken(tokenMap.get(token))
                    .leg(leg)
                    .build())
            ))
        .collect(toMap(Pair::first, Pair::second));

    return new LinkableTokens(legMap, tokenMap.keySet());
  }

  /**
   * We are going to want deterministic ordering between runs here - enforce the use of a {@link LinkedHashMap}.
   */
  public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> toMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper) {
    return Collectors.toMap(keyMapper, valueMapper, (a, b) -> b, LinkedHashMap::new);
  }

  private static void ifDebugEnabled(Consumer<Logger> log) {
    if (LOG.isDebugEnabled()) {
      log.accept(LOG);
    }
  }

  private static void ifTraceEnabled(Consumer<Logger> log) {
    if (LOG.isTraceEnabled()) {
      log.accept(LOG);
    }
  }

  private Set<Leg> logResolvedPoints(String type, Set<Leg> legs) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("- Resolved {} points:", type);
      LOG.debug(String.format("  %20s %10s", "Fix", "Terminator"));
      legs.forEach(leg -> LOG.debug(
              String.format("  %20s %10s",
                  leg.associatedFix().map(Fix::fixIdentifier).orElse("None"),
                  leg.pathTerminator().name()
              )
          )
      );
    }
    return legs;
  }

  private void logPathCandidate(GraphPath<Leg, DefaultWeightedEdge> path) {
    if (LOG.isDebugEnabled()) {
      List<Leg> vertices = path.getVertexList();
      LOG.debug(
          String.format(
              "  %10s %10s %10s %30s",
              vertices.get(0).associatedFix().map(Fix::fixIdentifier).orElse("None"),
              vertices.get(vertices.size() - 1).associatedFix().map(Fix::fixIdentifier).orElse("None"),
              vertices.size(),
              path.getWeight()
          )
      );
    }
  }

  /**
   * Similar to {@link ResolvedTokens} but for their linkable for - see {@link #make(ResolvedTokens)} for context.
   */
  private static final class LinkableTokens {

    private final Map<Leg, LinkableLeg> linkableLegs;

    private final Collection<LinkableToken> tokens;

    private LinkableTokens(Map<Leg, LinkableLeg> linkableLegs, Collection<LinkableToken> tokens) {
      this.linkableLegs = linkableLegs;
      this.tokens = tokens;
    }

    boolean nonEmpty() {
      return !linkableLegs.isEmpty();
    }

    /**
     * Some linkers will create new legs (e.g. Any->Approach), it's hard in here to decide what RouteToken/ResolvedToken should
     * be their parent, so we defer this until later on.
     *
     * <p>See {@link #fixupInventedLegs(List)}.
     */
    LinkableLeg linkableLeg(Leg leg) {
      return ofNullable(linkableLegs.get(leg)).orElseGet(() -> LinkableLeg.builder().leg(leg).build());
    }

    Collection<LinkedLegs> graphRepresentation() {
      return tokens.stream()
          .flatMap(token -> token.graphRepresentation().stream())
          .map(this::rewrap)
          .collect(toList());
    }

    Collection<LinkedLegs> links(LinkableTokens linkableTokens) {
      return cartesianProduct(linkableTokens(), linkableTokens.linkableTokens()).stream()
          .flatMap(pair -> pair.first().accept(pair.second()).links().stream()
              .map(linkedLegs -> new LinkedLegs(
                  linkableLeg(linkedLegs.source()),
                  linkableTokens.linkableLeg(linkedLegs.target()),
                  linkedLegs.linkWeight()
              )))
          .collect(toList());
    }

    private Collection<LinkableToken> linkableTokens() {
      return tokens;
    }

    private LinkedLegs rewrap(LinkedLegs linkedLegs) {
      return new LinkedLegs(
          linkableLegs.get(linkedLegs.source()),
          linkableLegs.get(linkedLegs.target()),
          linkedLegs.linkWeight()
      );
    }
  }

  /**
   * Wrapper around a {@link Leg} marked with source information, see {@link #make(ResolvedTokens)} for context.
   *
   * <p>The {@link #routeToken()} and {@link #resolvedToken()} are left optional as some legs may be invented... we can deal with
   * this later on in the chooser implementation.
   */
  private static final class LinkableLeg implements Leg {

    private final RouteToken routeToken;

    private final ResolvedToken resolvedToken;

    private final Leg leg;

    private LinkableLeg(Builder builder) {
      this.routeToken = builder.routeToken;
      this.resolvedToken = builder.resolvedToken;
      this.leg = requireNonNull(builder.leg);
    }

    public static Builder builder() {
      return new Builder();
    }

    Optional<RouteToken> routeToken() {
      return ofNullable(routeToken);
    }

    Optional<ResolvedToken> resolvedToken() {
      return ofNullable(resolvedToken);
    }

    Leg leg() {
      return leg;
    }

    @Override
    public Optional<? extends Fix> associatedFix() {
      return leg.associatedFix();
    }

    @Override
    public Optional<? extends Fix> recommendedNavaid() {
      return leg.recommendedNavaid();
    }

    @Override
    public Optional<? extends Fix> centerFix() {
      return leg.centerFix();
    }

    @Override
    public PathTerminator pathTerminator() {
      return leg.pathTerminator();
    }

    @Override
    public int sequenceNumber() {
      return leg.sequenceNumber();
    }

    @Override
    public Optional<Double> outboundMagneticCourse() {
      return leg.outboundMagneticCourse();
    }

    @Override
    public Optional<Double> rho() {
      return leg.rho();
    }

    @Override
    public Optional<Double> theta() {
      return leg.theta();
    }

    @Override
    public Optional<Double> rnp() {
      return leg.rnp();
    }

    @Override
    public Optional<Double> routeDistance() {
      return leg.routeDistance();
    }

    @Override
    public Optional<Duration> holdTime() {
      return leg.holdTime();
    }

    @Override
    public Optional<Double> verticalAngle() {
      return leg.verticalAngle();
    }

    @Override
    public Range<Double> speedConstraint() {
      return leg.speedConstraint();
    }

    @Override
    public Range<Double> altitudeConstraint() {
      return leg.altitudeConstraint();
    }

    @Override
    public Optional<TurnDirection> turnDirection() {
      return leg.turnDirection();
    }

    @Override
    public boolean isFlyOverFix() {
      return leg.isFlyOverFix();
    }

    @Override
    public boolean isPublishedHoldingFix() {
      return leg.isPublishedHoldingFix();
    }

    public Builder toBuilder() {
      return new Builder().routeToken(routeToken).resolvedToken(resolvedToken).leg(leg);
    }

    @Override
    public void accept(Visitor visitor) {
      leg.accept(visitor);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      LinkableLeg that = (LinkableLeg) o;
      return Objects.equals(routeToken, that.routeToken) && Objects.equals(resolvedToken, that.resolvedToken) && Objects.equals(leg, that.leg);
    }

    @Override
    public int hashCode() {
      return Objects.hash(routeToken, resolvedToken, leg);
    }

    private static final class Builder {

      private RouteToken routeToken;

      private ResolvedToken resolvedToken;

      private Leg leg;

      private Builder() {
      }

      public Builder routeToken(RouteToken routeToken) {
        this.routeToken = routeToken;
        return this;
      }

      public Builder resolvedToken(ResolvedToken resolvedToken) {
        this.resolvedToken = resolvedToken;
        return this;
      }

      public Builder leg(Leg leg) {
        this.leg = requireNonNull(leg);
        return this;
      }

      public LinkableLeg build() {
        return new LinkableLeg(this);
      }
    }
  }
}
