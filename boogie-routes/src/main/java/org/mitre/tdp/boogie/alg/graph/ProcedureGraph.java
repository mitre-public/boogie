package org.mitre.tdp.boogie.alg.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.LowestCommonAncestorAlgorithm;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.models.Procedure;
import org.mitre.tdp.boogie.service.impl.NameLocationService;
import org.mitre.tdp.boogie.utils.Collections;
import org.mitre.tdp.boogie.utils.Iterators;

import static org.mitre.tdp.boogie.utils.Collections.allMatch;

/**
 * Representation of the procedure built from its collection of transitions as a graph object.
 * <p>
 * Using jgrapht as the engine we leverage a whole host of common graph algorithms on the procedure
 * treating it as a directed graph. For the route expansion use case the most important are:
 * <p>
 * {@link ConnectivityInspector}
 * {@link AllDirectedPaths}
 * {@link LowestCommonAncestorAlgorithm}
 */
public class ProcedureGraph extends SimpleDirectedGraph<Leg, DefaultEdge> implements Procedure {

  private final Collection<Transition> transitions;
  private final NameLocationService<Leg> nls;

  private AllDirectedPaths<Leg, DefaultEdge> allPaths;

  private ProcedureGraph(Collection<? extends Transition> transitions, NameLocationService<Leg> nls) {
    super(DefaultEdge.class);
    this.transitions = (Collection<Transition>) transitions;
    this.nls = nls;
  }

  @Override
  public Collection<Transition> transitions() {
    return transitions;
  }

  /**
   * Returns the leg of the preferred type which best matches the specified fix. This lookup is done
   * both by name as well as geospatially if the fix identifier doesn't exist in the procedure.
   */
  private Leg bestLegMatch(Fix fix) {
    Collection<Leg> nameMatches = nls.matches(fix.identifier());
    return nameMatches.stream()
        .min(Comparator.comparing(Leg::type))
        .orElse(nls.nearest(fix.latLong()));
  }

  @Override
  public List<List<Leg>> pathsBetween(Fix entry, Fix exit) {
    if (allPaths == null) {
      allPaths = new AllDirectedPaths<>(this);
    }
    List<GraphPath<Leg, DefaultEdge>> gpaths =
        allPaths.getAllPaths(
            bestLegMatch(entry),
            bestLegMatch(exit),
            false,
            100);
    return Collections.transform(gpaths, GraphPath::getVertexList);
  }

  /**
   * Constructs a procedure graph object from the collection of transitions associated with a particular procedure.
   */
  public static ProcedureGraph from(Collection<? extends Transition> transitions) {
    Preconditions.checkArgument(allMatch(transitions, Transition::procedure));
    Preconditions.checkArgument(allMatch(transitions, Transition::procedureType));
    Preconditions.checkArgument(allMatch(transitions, Transition::airport));
    Preconditions.checkArgument(allMatch(transitions, Transition::source));

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
      if (transition.legs().size() == 1) {
        Leg leg = (Leg) transition.legs().get(0);
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

    if (!procedure.type().equals(ProcedureType.APPROACH)) {
      TransitionTriple.from(transitions).zipAndInsert(procedure);
    }
    return procedure;
  }
}
