package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.util.TransitionSorter;

/**
 * Represents a logical procedure object which is composed of a collection of {@link Transition}s.
 * <br>
 * These transition contain sequences of legs which represent linear paths through portions of the procedure as a whole.
 * <br>
 * When aircraft file for a procedure typically the runway they are arriving to/departing from plus the indication of an entry
 * or exit fix define a unique path through the otherwise more complex procedure DAG (directed acyclic graph - at least in the
 * SID/STAR case).
 */
public interface Procedure {

  static Procedure transitionMasked(Procedure procedure, Predicate<Transition> transitionFilter) {
    return new TransitionMaskedProcedure(procedure, transitionFilter);
  }

  static Procedure onlyEnrouteCommon(Procedure procedure) {
    return transitionMasked(procedure, t -> TransitionType.COMMON.equals(t.transitionType()) | TransitionType.ENROUTE.equals(t.transitionType()));
  }

  static Procedure onlyRunway(Procedure procedure) {
    return transitionMasked(procedure, t -> TransitionType.RUNWAY.equals(t.transitionType()));
  }

  /**
   * The identifier for the procedure.
   * <br>
   * e.g. GLAVN1, HOBBT2, GNDLF1
   */
  String procedureIdentifier();

  /**
   * The identifier of the airport the procedure serves.
   */
  String airportIdentifier();

  /**
   * The region of the airport the procedure serves is in.
   */
  String airportRegion();

  /**
   * See {@link ProcedureType} - indicates where and in what way the procedure is supposed to be used.
   */
  ProcedureType procedureType();

  /**
   * See {@link RequiredNavigationEquipage} - this indicates at a high level the required equipage needed by aircraft to fly the
   * procedure.
   * * <br>
   * Often-times to get this label correct (as this higher-level categorization isn't typically in raw navigational data sources)
   * all of the procedure's component transitions need to be inspected - as such this lives here an <i>not</i> at the transition
   * level.
   */
  RequiredNavigationEquipage requiredNavigationEquipage();

  /**
   * Collection of all the {@link Transition}s referenced by the procedure.
   */
  Collection<? extends Transition> transitions();

  /**
   * Returns the collection of transitions which represent the "initial" or "entry" transitions into the Procedure. Preference
   * order for these by {@link ProcedureType} is:
   * <ol>
   *   <li>SID - runway > common > enroute</li>
   *   <li>STAR - enroute > common > runway</li>
   *   <li>APPROACH - approach > common > ruwnay > missed</li>
   * </ol>
   *
   * <p>This method is intended to help identify candidate entry points where aircraft are likely to merge onto the procedure.
   */
  default List<Transition> initialTransitions() {
    return TransitionSorter.INSTANCE.apply(transitions()).stream().filter(col -> !col.isEmpty()).findFirst().orElseGet(List::of);
  }

  /**
   * Returns the collection of transitions which represent the "final" or "exit" transitions into the Procedure. Preference
   * order for these by {@link ProcedureType} is:
   * <ol>
   *   <li>SID - enroute > common > runway</li>
   *   <li>STAR - runway > common > enroute</li>
   *   <li>APPROACH - missed > runway > common > approach</li>
   * </ol>
   *
   * <p>This method is intended to help identify candidate exit points where aircraft are likely to leave the procedure.
   */
  default List<Transition> finalTransitions() {
    return TransitionSorter.INSTANCE.apply(transitions()).stream().filter(col -> !col.isEmpty()).reduce((t1, t2) -> t2).orElseGet(List::of);
  }

  /**
   * Returns the entry legs of all {@link #initialTransitions()} which match the provided predicate. This is intended to support
   * clients looking to find the standard "entry points" for a procedure.
   *
   * @param filter an optional filter for the legs, e.g. {@code leg -> leg.associatedFix().isPresent()}.
   */
  default List<Leg> entryLegs(Predicate<Leg> filter) {
    return initialTransitions().stream().flatMap(transition -> transition.legs().stream().filter(filter).findFirst().stream()).collect(toList());
  }

  /**
   * Returns the entry legs of all {@link #finalTransitions()} which match the provided predicate. This is intended to support
   * clients looking to find the standard "exit points" for a procedure.
   *
   * @param filter an optional filter for the legs, e.g. {@code leg -> leg.associatedFix().isPresent()}.
   */
  default List<Leg> exitLegs(Predicate<Leg> filter) {
    return finalTransitions().stream().flatMap(transition -> transition.legs().stream().filter(filter).reduce((l1, l2) -> l2).stream()).collect(toList());
  }

  final class TransitionMaskedProcedure implements Procedure {

    private final Procedure procedure;

    private final List<Transition> filteredTransitions;

    private TransitionMaskedProcedure(Procedure procedure, Predicate<Transition> transitionFilter) {
      this.procedure = requireNonNull(procedure);
      requireNonNull(transitionFilter, "Transition filter must be provided");
      this.filteredTransitions = procedure.transitions().stream().filter(transitionFilter).collect(toList());
    }

    @Override
    public String procedureIdentifier() {
      return procedure.procedureIdentifier();
    }

    @Override
    public String airportIdentifier() {
      return procedure.airportIdentifier();
    }

    @Override
    public String airportRegion() {
      return procedure.airportRegion();
    }

    @Override
    public ProcedureType procedureType() {
      return procedure.procedureType();
    }

    @Override
    public RequiredNavigationEquipage requiredNavigationEquipage() {
      return procedure.requiredNavigationEquipage();
    }

    @Override
    public Collection<? extends Transition> transitions() {
      return filteredTransitions;
    }
  }
}