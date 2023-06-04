package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;
import static org.mitre.tdp.boogie.util.ConditionalLogging.logIf;
import static org.mitre.tdp.boogie.util.Iterators.fastslow;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkableToken;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkedLegs;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkingStrategy;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenGrapher;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.util.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;

final class GraphicalRouteChooser implements RouteChooser {

  private static final Logger LOG = LoggerFactory.getLogger(GraphicalRouteChooser.class);

  private final TokenMapper mapper;

//  private final TokenGrapher tokenGrapher;

  private final LinkingStrategy linkingStrategy;

  GraphicalRouteChooser(TokenGrapher tokenGrapher, LinkingStrategy linkingStrategy) {
    this.mapper = TokenMapper.standard();
//    this.tokenGrapher = requireNonNull(tokenGrapher);
    this.linkingStrategy = requireNonNull(linkingStrategy);
  }

  @Override
  public List<ResolvedLeg> chooseRoute(List<ResolvedTokens> resolvedTokens) {
    if (!Iterators.checkMatchCount(resolvedTokens, nonEmpty())) {
      LOG.warn("At least two resolved sections must provide leg-level information to choose path.");
      return List.of();
    }
    return resolvedLegSequence(resolvedTokens);
  }

  /**
   * Returns the sequence of {@link ResolvedLeg}s as returned from the graphical resolution of the route.
   */
  private List<ResolvedLeg> resolvedLegSequence(List<ResolvedTokens> resolvedTokens) {

    List<LinkableTokens> linkableTokens = resolvedTokens.stream()
        .map(this::makeLinkable)
        .collect(toList());

    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> routeGraph = constructRouteGraph2(linkableTokens);
    LOG.debug("Constructed the following graph:\n {}.", GraphExporter.INSTANCE.apply(routeGraph));
    

    /*
     * TODO: Think about implications of the graph hashing legs as vertices...
     * Making Leg implementations into value objects (something people may expect) has implications on the graph-based
     * expander which is expecting to be able to handle flattened legs from different source objects by identity to re-
     * associate them with those source objects later to build the ResolvedLegs..
     */
    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> routeGraph = constructRouteGraph(resolvedTokens);
    LOG.debug("Constructed the following graph:\n {}.", GraphExporter.INSTANCE.apply(routeGraph));

    Set<Leg> resolvedEntryPoints = resolveEntryPoints(resolvedTokens);
    LOG.debug("Resolved {} candidate entry points into the route graph.", resolvedEntryPoints.size());

    Set<Leg> resolvedExitPoints = resolveExitPoints(resolvedTokens);
    LOG.debug("Resolved {} candidate exit points from the route graph.", resolvedExitPoints.size());

    DijkstraShortestPath<Leg, DefaultWeightedEdge> shortestPathAlgorithm = new DijkstraShortestPath<>(routeGraph);

    List<Leg> shortestPath = cartesianProduct(resolvedEntryPoints, resolvedExitPoints).stream()
        .map(pair -> shortestPathAlgorithm.getPath(pair.first(), pair.second()))
        .filter(Objects::nonNull)
        .peek(path -> LOG.debug("Identified shortest path of length {} with weight {}.", path.getWeight(), path.getVertexList().size()))
        .min(Comparator.comparing(GraphPath::getWeight))
        .map(GraphPath::getVertexList)
        .orElseGet(Collections::emptyList);

    LegEmbedding legEmbedding = newLegEmbedding(resolvedTokens);
    return shortestPath.stream().map(legEmbedding).collect(toList());
  }

  /**
   * Constructs a graphical representation of the resolved sections. This method uses the visitor pattern implemented on top of
   * the {@link ResolvedToken}s to generate links between consecutive sections.
   * <br>
   * The edges and their associated weights come from the returned {@link LinkedLegs}.
   */
  SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> constructRouteGraph(List<ResolvedTokens> resolvedTokens) {

    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    List<List<LinkableToken>> linkable = resolvedTokens.stream()
        .map(tokens -> tokens.resolvedTokens().stream()
            .map(mapper::map)
            .peek(token -> token.graphRepresentation().forEach(link -> addLinkedLegTo(graph, link)))
            .collect(toList()))
        .collect(toList());

    fastslow(linkable, list -> !list.isEmpty(), (previous, next, skip) -> cartesianProduct(previous, next)
        .forEach(pair -> pair.first().accept(pair.second()).links().forEach(link -> addLinkedLegTo(graph, link))));

    return graph;
  }

  SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> constructRouteGraph2(List<LinkableTokens> linkableTokens){

    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    return graph;
  }

  void addLinkedLegTo(SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph, LinkedLegs link) {
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
   * <br>
   * Generally speaking the returned collection will be a singleton leg representing the departure airport - but for cases where
   * we may have malformed route strings this formulation should remain robust.
   */
  LinkedHashSet<Leg> resolveEntryPoints(List<ResolvedTokens> resolvedTokens) {
    return resolvedTokens.stream()
        // drop sections without expanded legs (or with no source elements to expand)
        .filter(nonEmpty())
        .min(Comparator.comparingDouble(s -> s.routeToken().index()))
        .map(this::allLegs)
        .orElseGet(Collections::emptyList)
        .stream().map(LinkedLegs::source)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * Resolves the target exit points from the resolved set of sections to be the target legs of the linked legs in the last
   * resolved section of the route string.
   * <br>
   * Generally speaking the returned collection will be a singleton leg representing the arrival airport - but for cases where
   * we may have malformed route strings this formulation should remain robust.
   */
  LinkedHashSet<Leg> resolveExitPoints(List<ResolvedTokens> resolvedTokens) {
    return resolvedTokens.stream()
        .filter(nonEmpty())
        .max(Comparator.comparingDouble(s -> s.routeToken().index()))
        .map(this::allLegs)
        .orElseGet(Collections::emptyList)
        .stream().map(LinkedLegs::source)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Predicate<ResolvedTokens> nonEmpty() {
    return tokens -> !allLegs(tokens).isEmpty();
  }

  private Collection<LinkedLegs> allLegs(ResolvedTokens tokens) {
    return tokens.resolvedTokens().stream().flatMap(token -> tokenGrapher.graphRepresentationOf(token).stream()).collect(Collectors.toSet());
  }

  /**
   * Returns a newly initialized {@link LegEmbedding} with the appropriate leg->element and element->section lookups.
   */
  LegEmbedding newLegEmbedding(List<ResolvedTokens> resolvedTokens) {

    LinkedHashMultimap<Leg, ResolvedToken> legToElement = LinkedHashMultimap.create();
    LinkedHashMultimap<ResolvedToken, ResolvedTokens> elementToSection = LinkedHashMultimap.create();

    resolvedTokens.forEach(resolvedSection -> resolvedSection.resolvedTokens().forEach(resolvedElement -> {
      elementToSection.put(resolvedElement, resolvedSection);
      tokenGrapher.graphRepresentationOf(resolvedElement).forEach(linkedLeg -> {
        legToElement.put(linkedLeg.source(), resolvedElement);
        legToElement.put(linkedLeg.target(), resolvedElement);
      });
    }));

    return new LegEmbedding(legToElement, elementToSection);
  }

  private LinkableTokens makeLinkable(ResolvedTokens resolved) {
    return new LinkableTokens();
  }

  private static final class LinkableTokens {

    private RouteToken routeToken;

    private Map<LinkableToken, ResolvedToken> linkableTokens;


  }

  /**
   * Returns an embedding for mapping back from a given {@link Leg} returned by the route resolver to its source ResolvedElement
   * and ResolvedSection.
   *
   * <p>If multiple sections/elements generated/returned the same leg instance then this class will return the first section/element
   * associated with the leg.
   */
  private static final class LegEmbedding implements Function<Leg, ResolvedLeg> {

    private final LinkedHashMultimap<Leg, ResolvedToken> legToElement;
    private final LinkedHashMultimap<ResolvedToken, ResolvedTokens> elementToSection;

    LegEmbedding(
        LinkedHashMultimap<Leg, ResolvedToken> legToElement,
        LinkedHashMultimap<ResolvedToken, ResolvedTokens> elementToSection
    ) {
      this.legToElement = legToElement;
      this.elementToSection = elementToSection;
    }

    @Override
    public ResolvedLeg apply(Leg leg) {
      ResolvedToken resolvedToken = resolvedElementOf(leg);
      ResolvedTokens resolvedTokens = resolvedSectionOf(resolvedToken);

      return ResolvedLeg.create(resolvedTokens.routeToken(), resolvedToken, leg);
    }

    private ResolvedToken resolvedElementOf(Leg leg) {
      Collection<ResolvedToken> elements = legToElement.get(requireNonNull(leg));

      String resolvedElementTypes = elements.stream().map(resolvedElement -> resolvedElement.getClass().getTypeName()).collect(Collectors.joining(","));
      logIf(elements.size() > 1, LOG::debug, "Returned multiple source elements for leg: {}. ResolvedElement types were: {}.", leg, resolvedElementTypes);

      if (elements.isEmpty()) {
        return checkForManuallyAddedLeg(leg);
      }

      return elements.stream().findFirst().get();
    }

    private ResolvedTokens resolvedSectionOf(ResolvedToken resolvedToken) {
      Collection<ResolvedTokens> sections = elementToSection.get(requireNonNull(resolvedToken));

      String resolvedSections = sections.stream().map(resolvedSection -> resolvedSection.routeToken().toString()).collect(Collectors.joining(","));
      logIf(sections.size() > 1, LOG::warn, "Returned multiple source sections for element: {}. ResolvedSection splits were: {}.", resolvedToken, resolvedSections);

      return sections.stream().findFirst().orElseThrow(IllegalStateException::new);
    }

    private ResolvedToken checkForManuallyAddedLeg(Leg leg) {
      Preconditions.checkArgument(leg.pathTerminator().equals(PathTerminator.DF), "All manually added legs are DF legs");

      Leg originalLeg = legToElement.keys().stream()
          .filter(l -> l.associatedFix().isPresent())
          .filter(l -> l.associatedFix().get().fixIdentifier().equals(leg.associatedFix().get().fixIdentifier()))
          .findFirst()
          .orElseThrow(IllegalStateException::new);

      return resolvedElementOf(originalLeg);
    }
  }
}
