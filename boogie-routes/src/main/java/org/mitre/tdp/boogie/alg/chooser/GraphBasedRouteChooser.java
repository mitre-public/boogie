package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;
import static org.mitre.tdp.boogie.util.ConditionalLogging.logIf;
import static org.mitre.tdp.boogie.util.Iterators.fastslow;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import org.mitre.tdp.boogie.alg.ExpandedRoute;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.RouteSummary;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;

public final class GraphBasedRouteChooser implements RouteChooser {

  private static final Logger LOG = LoggerFactory.getLogger(GraphBasedRouteChooser.class);

  private static final Predicate<ResolvedSection> sectionIsNonEmpty = resolvedSection -> !resolvedSection.allLegs().isEmpty();

  /**
   * Fixer class to address the shortcomings of the current resolution strategy and make the route look more like one a FMS
   * would generate.
   * <br>
   * See the actual docs on {@link SubsequentDfToTfConverter} for motivation/details.
   */
  private static final SubsequentDfToTfConverter subsequentDFToTFConverter = new SubsequentDfToTfConverter();

  /**
   * Fixer class to merge leg definitions in combinations like TF->IF to remove the final IF from the leg sequence but persist
   * the lower of any restrictions between the two legs.
   * <br>
   * See the actual docs on the {@link SequentialLegCollapser} for further motivation/details.
   */
  private static final SequentialLegCollapser sequentialLegCollapser = new SequentialLegCollapser();

  /**
   * Pseudo-BiFunction implementation (interface clashes with functional) which allows clients to pass in a summarizer class
   * which will be called on the initial {@link ResolvedLeg} output of the chooser.
   */
  @Override
  public ExpandedRoute chooseRoute(List<ResolvedSection> resolvedSections) {

    List<ResolvedLeg> resolvedLegs = resolvedLegSequence(resolvedSections);
    LOG.debug("Generated {} total resolved legs.", resolvedLegs.size());

    List<ExpandedRouteLeg> expandedRouteLegs = subsequentDFToTFConverter.andThen(sequentialLegCollapser)
        .apply(resolvedLegs.stream().map(ResolvedLegConverter.INSTANCE).collect(Collectors.toList()));
    LOG.debug("Generated {} final expanded route legs.", expandedRouteLegs.size());

    Optional<RouteSummary> routeSummary = GraphicalRouteSummarizer.INSTANCE.apply(resolvedLegs);
    return new ExpandedRoute(routeSummary.orElse(null), expandedRouteLegs);
  }

  /**
   * Returns the sequence of {@link ResolvedLeg}s as returned from the graphical resolution of the route.
   */
  public List<ResolvedLeg> resolvedLegSequence(List<ResolvedSection> resolvedSections) {
    Preconditions.checkArgument(resolvedSections.stream().filter(sectionIsNonEmpty).count() >= 2,
        "At least two resolved sections must provide leg-level information.");

    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> routeGraph = constructRouteGraph(resolvedSections);
    LOG.debug("Constructed the following graph:\n {}.", GraphExporter.INSTANCE.apply(routeGraph));

    Set<Leg> resolvedEntryPoints = resolveEntryPoints(resolvedSections);
    LOG.debug("Resolved {} candidate entry points into the route graph.", resolvedEntryPoints.size());

    Set<Leg> resolvedExitPoints = resolveExitPoints(resolvedSections);
    LOG.debug("Resolved {} candidate exit points from the route graph.", resolvedExitPoints.size());

    DijkstraShortestPath<Leg, DefaultWeightedEdge> shortestPathAlgorithm = new DijkstraShortestPath<>(routeGraph);

    List<Leg> shortestPath = cartesianProduct(resolvedEntryPoints, resolvedExitPoints).stream()
        .map(pair -> shortestPathAlgorithm.getPath(pair.first(), pair.second()))
        .filter(Objects::nonNull)
        .peek(path -> LOG.debug("Identified shortest path of length {} with weight {}.", path.getWeight(), path.getVertexList().size()))
        .min(Comparator.comparing(GraphPath::getWeight))
        .map(GraphPath::getVertexList)
        .orElseGet(Collections::emptyList);

    LegEmbedding legEmbedding = newLegEmbedding(resolvedSections);
    return shortestPath.stream().map(legEmbedding).collect(Collectors.toList());
  }

  /**
   * Constructs a graphical representation of the resolved sections. This method uses the visitor pattern implemented on top of
   * the {@link ResolvedElement}s to generate links between consecutive sections.
   * <br>
   * The edges and their associated weights come from the returned {@link LinkedLegs}.
   */
  SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> constructRouteGraph(List<ResolvedSection> resolvedSections) {
    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    // add all the element-internal edges
    resolvedSections.forEach(resolvedSection -> resolvedSection.elements()
        .forEach(resolvedElement -> resolvedElement.toLinkedLegs().forEach(link -> addLinkedLegTo(graph, link))));

    // add the links between the adjacent ResolvedElements from various
    fastslow(resolvedSections, sectionIsNonEmpty, (previous, next, skip) -> cartesianProduct(previous.elements(), next.elements())
        .forEach(pair -> pair.first().linksTo(pair.second()).forEach(link -> addLinkedLegTo(graph, link))));

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
  LinkedHashSet<Leg> resolveEntryPoints(List<ResolvedSection> resolvedSections) {
    return resolvedSections.stream()
        // drop sections without expanded legs (or with no source elements to expand)
        .filter(resolvedSection -> !resolvedSection.allLegs().isEmpty())
        .min(Comparator.comparing(ResolvedSection::sectionSplit))
        .map(ResolvedSection::allLegs)
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
  LinkedHashSet<Leg> resolveExitPoints(List<ResolvedSection> resolvedSections) {
    return resolvedSections.stream()
        .filter(resolvedSection -> !resolvedSection.allLegs().isEmpty())
        .max(Comparator.comparing(ResolvedSection::sectionSplit))
        .map(ResolvedSection::allLegs)
        .orElseGet(Collections::emptyList)
        .stream().map(LinkedLegs::source)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * Returns a newly initialized {@link LegEmbedding} with the appropriate leg->element and element->section lookups.
   */
  LegEmbedding newLegEmbedding(List<ResolvedSection> resolvedSections) {
    LinkedHashMultimap<Leg, ResolvedElement> legToElement = LinkedHashMultimap.create();
    LinkedHashMultimap<ResolvedElement, ResolvedSection> elementToSection = LinkedHashMultimap.create();

    resolvedSections.forEach(resolvedSection -> resolvedSection.elements().forEach(resolvedElement -> {
      elementToSection.put(resolvedElement, resolvedSection);
      resolvedElement.toLinkedLegs().forEach(linkedLeg -> {
        legToElement.put(linkedLeg.source(), resolvedElement);
        legToElement.put(linkedLeg.target(), resolvedElement);
      });
    }));

    return new LegEmbedding(legToElement, elementToSection);
  }

  /**
   * Returns an embedding for mapping back from a given {@link Leg} returned by the route resolver to its source ResolvedElement
   * and ResolvedSection.
   * <br>
   * If multiple sections/elements generated/returned the same leg instance then this class will return the first section/element
   * associated with the leg.
   */
  private static final class LegEmbedding implements Function<Leg, ResolvedLeg> {

    private final LinkedHashMultimap<Leg, ResolvedElement> legToElement;
    private final LinkedHashMultimap<ResolvedElement, ResolvedSection> elementToSection;

    LegEmbedding(
        LinkedHashMultimap<Leg, ResolvedElement> legToElement,
        LinkedHashMultimap<ResolvedElement, ResolvedSection> elementToSection
    ) {
      this.legToElement = legToElement;
      this.elementToSection = elementToSection;
    }

    @Override
    public ResolvedLeg apply(Leg leg) {
      ResolvedElement resolvedElement = resolvedElementOf(leg);
      ResolvedSection resolvedSection = resolvedSectionOf(resolvedElement);

      return new ResolvedLeg(resolvedSection.sectionSplit(), resolvedElement, leg);
    }

    private ResolvedElement resolvedElementOf(Leg leg) {
      Collection<ResolvedElement> elements = legToElement.get(requireNonNull(leg));

      String resolvedElementTypes = elements.stream().map(resolvedElement -> resolvedElement.getClass().getTypeName()).collect(Collectors.joining(","));
      logIf(elements.size() > 1, LOG::debug, "Returned multiple source elements for leg: {}. ResolvedElement types were: {}.", leg, resolvedElementTypes);

      if (elements.isEmpty()) {
        return checkForManuallyAddedLeg(leg);
      }

      return elements.stream().findFirst().get();
    }

    private ResolvedSection resolvedSectionOf(ResolvedElement resolvedElement) {
      Collection<ResolvedSection> sections = elementToSection.get(requireNonNull(resolvedElement));

      String resolvedSections = sections.stream().map(resolvedSection -> resolvedSection.sectionSplit().toString()).collect(Collectors.joining(","));
      logIf(sections.size() > 1, LOG::warn, "Returned multiple source sections for element: {}. ResolvedSection splits were: {}.", resolvedElement, resolvedSections);

      return sections.stream().findFirst().orElseThrow(IllegalStateException::new);
    }

    private ResolvedElement checkForManuallyAddedLeg(Leg leg) {
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
