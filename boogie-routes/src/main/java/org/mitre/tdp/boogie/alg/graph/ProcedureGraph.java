package org.mitre.tdp.boogie.alg.graph;

import static org.mitre.tdp.boogie.util.Collections.allMatch;
import static org.mitre.tdp.boogie.util.Collections.transform;
import static org.mitre.tdp.boogie.util.Iterators.checkMatchCount;
import static org.mitre.tdp.boogie.util.Iterators.fastslow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.LowestCommonAncestorAlgorithm;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.service.impl.NameLocationService;
import org.mitre.tdp.boogie.util.Combinatorics;
import org.mitre.tdp.boogie.util.Iterators;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

/**
 * Representation of the procedure built from its collection of transitions as a graph object.
 *
 * <p>Using jgrapht as the engine we leverage a whole host of common graph algorithms on the procedure treating it as a directed
 * graph. For the route expansion use case the most important are:</p>
 *
 * {@link ConnectivityInspector}
 * {@link AllDirectedPaths}
 * {@link LowestCommonAncestorAlgorithm}
 */
public final class ProcedureGraph extends SimpleDirectedGraph<Leg, DefaultEdge> implements Procedure {

  private static final TransitionSorter sorter = new TransitionSorter();

  private final transient Collection<Transition> transitions;
  private final transient NameLocationService<Leg> nls;

  private transient AllDirectedPaths<Leg, DefaultEdge> allPaths;

  private ProcedureGraph(Collection<? extends Transition> transitions, NameLocationService<Leg> nls) {
    super(DefaultEdge.class);
    this.transitions = sorter.sort(transitions).stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    this.nls = nls;
  }

  @Override
  public Collection<Transition> transitions() {
    return transitions;
  }

  @Override
  public List<List<Leg>> pathsBetween(Fix entry, Fix exit) {
    if (allPaths == null) {
      allPaths = new AllDirectedPaths<>(this);
    }
    List<GraphPath<Leg, DefaultEdge>> gpaths = allPaths.getAllPaths(
        closestLegMatch(entry),
        closestLegMatch(exit),
        false,
        100
    );
    return transform(gpaths, GraphPath::getVertexList);
  }

  public List<Leg> legsTerminatingWith(Fix fix) {
    Collection<Leg> nameMatches = nls.matches(fix.identifier());
    return nameMatches.isEmpty()
        ? Collections.singletonList(nls.nearest(fix.latLong()))
        : new ArrayList<>(nameMatches);
  }

  /**
   * Returns the leg of the preferred type which best matches the specified fix. This lookup is done both by name as well as
   * geospatially if the fix identifier doesn't exist in the procedure.
   */
  public Leg closestLegMatch(Fix fix) {
    Collection<Leg> nameMatches = nls.matches(fix.identifier());
    Optional<Leg> match = nameMatches.stream().filter(leg -> fix.equals(leg.pathTerminator())).findFirst();
    return match.orElseGet(() -> nameMatches.stream().min(Comparator.comparing(Leg::type)).orElseGet(() -> nls.nearest(fix.latLong())));
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof ProcedureGraph && hashCode() == that.hashCode();
  }

  @Override
  public int hashCode() {
    return Objects.hash(transitions.toArray());
  }

  @Override
  public String toString() {
    return "ProcedureGraph: ".concat(Joiner.on(",").join(identifier(), airport(), type().name(), navigationSource().name()));
  }


  /**
   * Checker for the input collection of transitions to a given {@link ProcedureGraph}.
   */
  private static <T extends Transition> Collection<T> checkArgument(Collection<T> transitions) {
    Preconditions.checkArgument(!transitions.isEmpty());

    Preconditions.checkArgument(allMatch(transitions, Transition::procedure),
        String.join(",", transform(transitions, Transition::procedure)));

    Preconditions.checkArgument(allMatch(transitions, Transition::procedureType),
        String.join(",", transform(transitions, t -> t.procedureType().name())));

    Preconditions.checkArgument(allMatch(transitions, Transition::airport),
        String.join(",", transform(transitions, Transition::airport)));

    Preconditions.checkArgument(allMatch(transitions, Transition::navigationSource),
        String.join(",", transform(transitions, t -> t.navigationSource().name())));

    return transitions;
  }

  /**
   * Constructs a new {@link ProcedureGraph} from the sequenced collection of transitions generating links via a call to
   * {@link #generateEdges(List, List)} on the subsequent lists of input transitions.
   */
  public static <T extends Transition> ProcedureGraph from(List<List<T>> splitTransitions) {
    Collection<T> transitions = checkArgument(splitTransitions.stream().flatMap(Collection::stream).collect(Collectors.toList()));

    // find the terminators of the concrete leg types (these exist as actual fixes)
    Collection<Leg> concrete = transitions.stream()
        .map(Transition::legs)
        .flatMap(Collection::stream)
        .filter(leg -> leg.type().isConcrete())
        .collect(Collectors.toSet());

    NameLocationService nls = NameLocationService.from(
        concrete,
        leg -> leg.pathTerminator().identifier(),
        leg -> leg.pathTerminator().latLong());

    ProcedureGraph procedure = new ProcedureGraph(transitions, nls);

    // insert each of the transitions individually
    transitions.forEach(transition -> {
      if (transition.legs().size() == 0) {
        // no LEGS
      } else if (transition.legs().size() == 1) {
        Leg leg = transition.legs().get(0);
        procedure.addVertex(leg);
      } else {
        Iterators.pairwise(
            transition.legs(),
            (prev, curr) -> {
              procedure.addVertex(prev);
              procedure.addVertex(curr);
              procedure.addEdge(prev, curr);
            });
      }
    });

    if (checkMatchCount(splitTransitions, c -> !c.isEmpty())) {
      fastslow(splitTransitions, c -> !c.isEmpty(), (l1, l2, skip) -> generateEdges(l1, l2).forEach(pair -> procedure.addEdge(pair.first(), pair.second())));
    }

    return procedure;
  }

  /**
   * Constructs a procedure graph object from the collection of transitions associated with a particular procedure.
   */
  public static ProcedureGraph from(Collection<? extends Transition> transitions) {
    return from(sorter.sort(transitions));
  }

  /**
   * Takes two collections of transitions assumed to be of following types and zips them together along their endpoints.
   *
   * <p>e.g. List<ENROUTE> -> List<COMMON>
   */
  public static <T extends Transition> List<Pair<Leg, Leg>> generateEdges(List<T> previous, List<T> next) {
    // these should all have concrete fixes as path terminators
    List<Leg> terminals = transform(previous, t -> ((List<Leg>) t.legs()).get(t.legs().size() - 1));
    List<Leg> initials = transform(next, t -> ((List<Leg>) t.legs()).get(0));

    List<Pair<Leg, Leg>> edges = new ArrayList<>();

    Iterator<Pair<Leg, Leg>> paired = Combinatorics.cartesianProduct(terminals::iterator, initials::iterator);
    paired.forEachRemaining(pair -> {

      // occasionally transition will end/start with non-concrete leg types (no associated fix) we can't
      // zip these together but we can pass them through without failing
      String firstIdentifier = Optional.ofNullable(pair.first())
          .map(Leg::pathTerminator)
          .map(Fix::identifier)
          .orElse(null);

      String secondIdentifier = Optional.ofNullable(pair.second())
          .map(Leg::pathTerminator)
          .map(Fix::identifier)
          .orElse(null);

      if (firstIdentifier != null && firstIdentifier.equals(secondIdentifier)) {
        edges.add(Pair.of(pair.first(), pair.second()));
      }
    });

    return edges;
  }
}
