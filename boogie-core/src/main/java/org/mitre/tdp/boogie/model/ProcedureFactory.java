package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TraversalOrderSorter;
import org.mitre.tdp.boogie.util.Combinatorics;
import org.mitre.tdp.boogie.util.Iterators;
import org.mitre.tdp.boogie.util.Streams;

/**
 * Due to the fact that {@link Procedure}s are generally compositions of {@link Transition}s, and there are standard ways to
 * combine transitions to make procedures, Boogie provides a couple of common factory methods to instantiate procedure objects
 * from collections of transitions.
 */
public final class ProcedureFactory {

  private ProcedureFactory() {
    throw new IllegalStateException("Cannot instantiate static factory class.");
  }

  /**
   * Builds and decorates the supplied {@link Procedure} record as a {@link ProcedureGraph} - which represents the legs within
   * the source procedure in a graphical manner.
   */
  public static ProcedureGraph newProcedureGraph(Procedure procedure) {
    requireNonNull(procedure);

    ProcedureGraph graph = new ProcedureGraph(procedure);

    // add all transition legs and their associated leg->leg edges
    procedure.transitions().forEach(transition -> {
      transition.legs().forEach(graph::addVertex);
      Streams.pairwise(transition.legs()).forEach(pair -> graph.addEdge(pair.first(), pair.second()));
    });

    // split the transitions by type and insert edges between initial/final legs of subsequent transitions as long
    // as they share a fix identifier (e.g. TF at end of ENROUTE transition -> IF at start of COMMON)
    List<List<Transition>> sortedTransitions = TraversalOrderSorter.forProcedureType(procedure.procedureType()).sort(procedure.transitions());
    if (Iterators.checkMatchCount(sortedTransitions, col -> !col.isEmpty())) {
      Iterators.fastslow(
          sortedTransitions,
          col -> !col.isEmpty(),
          (prev, next, skip) -> transitionLinks(prev, next).forEach(pair -> graph.addEdge(pair.first(), pair.second()))
      );
    }

    return graph;
  }

  /**
   * Returns the collection of paired final->initial legs from the input collections of (assumed sorted) transitions - filtered
   * to only those pairs who's {@link Leg#associatedFix()}s are populated and have matching identifiers.
   */
  private static Stream<Pair<Leg, Leg>> transitionLinks(Collection<Transition> col1, Collection<Transition> col2) {

    Set<Leg> finalLegs = col1.stream().map(ProcedureFactory::finalLegOf)
        .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());

    Set<Leg> initialLegs = col2.stream().map(ProcedureFactory::initialLegOf)
        .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());

    return Combinatorics.cartesianProduct(finalLegs, initialLegs).stream()
        .filter(legPair -> legPair.first().associatedFix().isPresent())
        .filter(legPair -> legPair.second().associatedFix().isPresent())
        .filter(legPair -> legPair.first().associatedFix().get().fixIdentifier().equals(legPair.second().associatedFix().get().fixIdentifier()))
        // remove any pairs where the legs are literally identical - these will be linked implicitly anyway when the graphs
        // edge set is iterated over
        .filter(legPair -> !legPair.first().equals(legPair.second()));
  }

  private static Optional<Leg> initialLegOf(Transition transition) {
    return transition.legs().isEmpty() ? Optional.empty() : Optional.of(transition.legs().get(0));
  }

  private static Optional<Leg> finalLegOf(Transition transition) {
    return transition.legs().isEmpty() ? Optional.empty() : Optional.of(transition.legs().get(transition.legs().size() - 1));
  }
}
