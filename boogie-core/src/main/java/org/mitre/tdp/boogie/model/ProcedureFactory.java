package org.mitre.tdp.boogie.model;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.CategoryOrType;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.TraversalOrderSorter;
import org.mitre.tdp.boogie.fn.TriConsumer;
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
    procedure.transitions().forEach(transition -> ProcedureFactory.legToGraph(transition, graph));

    // split the transitions by type and insert edges between initial/final legs of subsequent transitions as long
    // as they share a fix identifier (e.g. TF at end of ENROUTE transition -> IF at start of COMMON)
    List<List<Transition>> sortedTransitions = TraversalOrderSorter.forProcedureType(procedure.procedureType()).sort(procedure.transitions());
    Predicate<List<Transition>> notEmpty = col -> !col.isEmpty();
    TriConsumer<List<Transition>, List<Transition>, Boolean> linkThings = switch (procedure.procedureType()) {
      case SID, STAR -> (prev, next, skip) -> sidStarLinks(prev, next).forEach(pair -> graph.addEdge(pair.first(), pair.second()));
      case APPROACH -> (prev, next, skip) -> approachLinks(prev, next).forEach(pair -> graph.addEdge(pair.first(), pair.second()));
    };

    if (Iterators.checkMatchCount(sortedTransitions, col -> !col.isEmpty())) {
      Iterators.fastslow(sortedTransitions, notEmpty, linkThings);
    }

    return graph;
  }

  private static void legToGraph(Transition transition, ProcedureGraph graph) {
    transition.legs().forEach(graph::addVertex);
    Streams.pairwise(transition.legs()).forEach(pair -> graph.addEdge(pair.first(), pair.second()));
  }

  /**
   * Returns the collection of paired final->initial legs from the input collections of (assumed sorted) transitions - filtered
   * to only those pairs who's {@link Leg#associatedFix()}s are populated and have matching identifiers.
   * <p>
   * This is for approach where we have some special rules about things that actually don't link by-data-as-written (only by rule)
   * and joining the final basically anywhere (IRL vs. as expected at IF IF/FACF).
   */
  private static Stream<Pair<Leg, Leg>> approachLinks(Collection<Transition> col1, Collection<Transition> col2) {
    Set<SortableLeg> finalLegs = allEndingLegs(col1);
    Set<SortableLeg> flyTo = allStartingLegs(col2);

    Map<SortableLeg, List<SortableLeg>> sortedLegs = linksBetween(finalLegs, flyTo)
        .collect(Collectors.groupingBy(Pair::first, Collectors.mapping(Pair::second, Collectors.toList())));

    finalLegs.stream()
        .filter(l -> TransitionType.APPROACH.equals(l.transitionType()))
        //some e.g., CAT_A approach transitions join to the final at strange places in certain places.
        .forEach(leg -> sortedLegs.computeIfAbsent(leg, (l) -> missingLink(l, col2)));

    return sortedLegs.entrySet().stream()
        .map(e -> Combinatorics.cartesianProduct(List.of(e.getKey()), e.getValue()))
        .flatMap(Collection::stream)
        .filter(pair -> !pair.first().leg().equals(pair.second().leg()))
        .map(pair -> Pair.of(pair.first().leg(),  pair.second().leg()));
  }

  /**
   * This method will scan through the next transitions for a non-starting leg that can link the approach transition.
   * @param leg the leg missing a link
   * @param col2 the final transitions aka "common"
   * @return our link or null.
   */
  private static List<SortableLeg> missingLink(SortableLeg leg, Collection<Transition> col2) {
    return col2.stream()
        .filter(t -> TransitionType.COMMON.equals(t.transitionType()))
        .flatMap(t -> t.legs().stream().skip(1).map(l -> new SortableLeg(t.categoryOrTypes(), t.transitionType(), l)))
        .filter(l -> SAME_FIX.test(Pair.of(leg, l)))
        .findAny()
        .map(List::of)
        .orElse(null);
  }

  /**
   * Returns the collection of paired final->initial legs from the input collections of (assumed sorted) transitions - filtered
   * to only those pairs who's {@link Leg#associatedFix()}s are populated and have matching identifiers.
   */
  private static Stream<Pair<Leg, Leg>> sidStarLinks(Collection<Transition> col1, Collection<Transition> col2) {
    Set<SortableLeg> finalLegs = allEndingLegs(col1);
    Set<SortableLeg> initialLegs = allStartingLegs(col2);
    return linksBetween(finalLegs, initialLegs)
        .map(pair -> Pair.of(pair.first().leg(),  pair.second().leg()))
        .filter(pair -> !pair.first().equals(pair.second()));
  }

  /**
   * This grabs the final leg of the groups into a set.
   * @param col1 the previous transitions
   * @return all the last legs.
   */
  private static Set<SortableLeg> allEndingLegs(Collection<Transition> col1) {
    return col1.stream()
        .map(i -> ProcedureFactory.finalLegOf(i).map(l -> new SortableLeg(i.categoryOrTypes(), i.transitionType(), l)))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  /**
   * This grabs the initial leg of all the next transitions
   * @param col2 the next ones
   * @return all the first legs
   */
  private static Set<SortableLeg> allStartingLegs(Collection<Transition> col2) {
    return col2.stream()
        .map(i -> ProcedureFactory.initialLegOf(i).map(l -> new SortableLeg(i.categoryOrTypes(), i.transitionType(), l)))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  /**
   * This is the logic to say yes this should link.
   * @param finalLegs the previous legs
   * @param initialLegs the next legs
   * @return links in a stream.
   */
  private static Stream<Pair<SortableLeg, SortableLeg>> linksBetween(Set<SortableLeg> finalLegs, Set<SortableLeg> initialLegs) {
    return Combinatorics.cartesianProduct(finalLegs, initialLegs).stream()
        .filter(NEXT_SUPPORTS_THIS_TRANSITION)
        .filter(SAME_FIX.or(XM_INTO_ANYTHING).or(ENDS_IN_IF).or(COMMON_INTO_MISSED).or(APPROACH_ENDS_ON_XI).or(HF_INTO_COMMON))
        // remove any pairs where the legs are literally identical - these will be linked implicitly anyway when the graphs
        // edge set is iterated over ... which cant happen in good data but better to prevent the duplicate
        .filter(legPair -> !legPair.first().equals(legPair.second()));
  }

  private static final Predicate<Pair<SortableLeg, SortableLeg>> SAME_FIX = legLegPair -> Optional.of(legLegPair)
      .filter(legPair -> legPair.first().leg().associatedFix().isPresent())
      .filter(legPair -> legPair.second().leg().associatedFix().isPresent())
      .filter(legPair -> legPair.first().leg().associatedFix().get().fixIdentifier().equals(legPair.second().leg().associatedFix().get().fixIdentifier()))
      .isPresent();

  //just link to whatever
  private static final Predicate<Pair<SortableLeg, SortableLeg>> XM_INTO_ANYTHING = legPair -> legPair.first().leg().pathTerminator().isManualTerminating();

  private static final Predicate<Pair<SortableLeg, SortableLeg>> HF_INTO_COMMON = legPair -> PathTerminator.HF.equals(legPair.first().leg().pathTerminator())
      && TransitionType.COMMON.equals(legPair.second().transitionType())
      && TransitionType.APPROACH.equals(legPair.first().transitionType());

  //this is when there is just the one leg (rule in the data) which per att5 means link it... dangerous to assume rules are followed.
  private static final Predicate<Pair<SortableLeg, SortableLeg>> ENDS_IN_IF = legPair -> PathTerminator.IF.equals(legPair.first().leg().pathTerminator());

  private static final Predicate<Pair<SortableLeg, SortableLeg>> COMMON_INTO_MISSED = legPair -> Optional.of(legPair)
      .filter(i -> TransitionType.COMMON.equals(i.first().transitionType()))
      .filter(i -> TransitionType.MISSED.equals(i.second().transitionType))
      .isPresent();

  //we don't really care this only applies to ILS approach.
  private static final Predicate<Pair<SortableLeg, SortableLeg>> APPROACH_ENDS_ON_XI = legPair -> Optional.of(legPair)
      .filter(pair -> pair.first().transitionType().equals(TransitionType.APPROACH))
      .filter(pair -> pair.first().leg().pathTerminator().isIntercept())
      .isPresent();

  private static final Predicate<Pair<SortableLeg, SortableLeg>> NEXT_SUPPORTS_THIS_TRANSITION = legPair -> (isNull(legPair.first().types()) || isNull(legPair.second().types()))
      || (legPair.first().types().isEmpty() || legPair.second().types().isEmpty())
      || legPair.second().types().contains(CategoryOrType.NOT_SPECIFIED)
      || legPair.first().types().contains(CategoryOrType.NOT_SPECIFIED)
      || legPair.first().types().stream().anyMatch(t -> legPair.second().types().contains(t));

  private static Optional<Leg> initialLegOf(Transition transition) {
    return transition.legs().isEmpty() ? Optional.empty() : Optional.of(transition.legs().get(0));
  }

  private static Optional<Leg> finalLegOf(Transition transition) {
    return transition.legs().isEmpty() ? Optional.empty() : Optional.of(transition.legs().get(transition.legs().size() - 1));
  }

  private record SortableLeg(Set<CategoryOrType> types, TransitionType transitionType, Leg leg) {
    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SortableLeg that = (SortableLeg) o;
      return Objects.equals(leg, that.leg) && Objects.equals(types, that.types) && transitionType == that.transitionType;
    }

    @Override
    public int hashCode() {
      return Objects.hash(types, transitionType, leg);
    }
  }
}
