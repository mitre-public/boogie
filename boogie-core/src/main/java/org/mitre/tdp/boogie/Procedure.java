package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * In Boogie's simplified worldview it is sufficient to represent a procedure as a hierarchical piece of navigation infrastructure
 * containing a collection of {@link Transition}s (representing linear sequences of {@link Leg}s) with some simple metadata.
 */
public interface Procedure {

  /**
   * Returns a builder for a standard procedure implementation.
   *
   * <p>This should be used to construct lightweight procedure objects on the fly which are compliant with the Boogie APIs.
   */
  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  /**
   * Returns a new procedure {@link Record} representing a decorated client object and a Boogie {@link Procedure} implementation
   * the record object can delegate its method calls to.
   *
   * <p>This is provided to make it clear to clients that they can easily decorate and use their own objects as procedures with
   * much of Boogie's internal algorithms and then unwrap them later post-processing to get their objects back.
   *
   * @param datum    the client record type the procedure was derived from
   * @param delegate the procedure the delegate calls to (typically a {@link Standard})
   */
  static <T> Record<T> record(T datum, Procedure delegate) {
    return new Record<>(datum, delegate);
  }

  /**
   * Intended use is similar to {@link #record(Object, Procedure)}, but clarifies the contract that the delegated-to {@link Procedure}
   * impl should be derived from the input client data type.
   *
   * @param datum the client record type the procedure was derived from
   * @param maker function to construct the delegate procedure implementation from the client object
   */
  static <T> Record<T> make(T datum, Function<T, Procedure> maker) {
    return record(datum, requireNonNull(maker, "Should be non-null.").apply(datum));
  }

  /**
   * Returns a new view of the provided procedure with all transitions matching the provided filter masked.
   *
   * @param procedure  the procedure to decorate
   * @param shouldMask predicate returning true if a given transition should be masked (aka hidden when asking for transitions)
   */
  static Procedure maskTransitions(Procedure procedure, Predicate<Transition> shouldMask) {
    return new TransitionMaskedProcedure(procedure, shouldMask);
  }

  static Procedure onlyEnrouteCommon(Procedure procedure) {
    return maskTransitions(procedure, t -> TransitionType.RUNWAY.equals(t.transitionType()));
  }

  static Procedure onlyRunway(Procedure procedure) {
    return maskTransitions(procedure, t -> TransitionType.COMMON.equals(t.transitionType()) | TransitionType.ENROUTE.equals(t.transitionType()));
  }

  /**
   * The identifier for the procedure e.g. {@code GLAVN1, HOBBT2, GNDLF1}.
   */
  String procedureIdentifier();

  /**
   * The identifier of the airport the procedure serves.
   */
  String airportIdentifier();

  /**
   * See {@link ProcedureType} - indicates where and in what way the procedure is supposed to be used.
   */
  ProcedureType procedureType();

  /**
   * See {@link RequiredNavigationEquipage} - this indicates at a high level the required equipage needed by aircraft to fly the
   * procedure.
   *
   * <p>This equipage value isn't generally summarized this neatly (424 goes into gory detail about equipage) but for use-cases
   * across Boogie we typically only care to this granularity.
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
   *   <li>SID - {@code runway > common > enroute}</li>
   *   <li>STAR - {@code enroute > common > runway}</li>
   *   <li>APPROACH - {@code approach > common > runway > missed}</li>
   * </ol>
   *
   * <p>This method is intended to help identify candidate entry points where aircraft are likely to merge onto the procedure.
   */
  default List<Transition> initialTransitions() {
    return TraversalOrderSorter.forProcedureType(procedureType()).sort(transitions()).stream().filter(col -> !col.isEmpty()).findFirst().orElseGet(List::of);
  }

  /**
   * Returns the collection of transitions which represent the "final" or "exit" transitions into the Procedure. Preference
   * order for these by {@link ProcedureType} is:
   * <ol>
   *   <li>SID - {@code runway > common > enroute}</li>
   *   <li>STAR - {@code enroute > common > runway}</li>
   *   <li>APPROACH - {@code approach > common > runway > missed}</li>
   * </ol>
   *
   * <p>This method is intended to help identify candidate exit points where aircraft are likely to leave the procedure.
   */
  default List<Transition> finalTransitions() {
    return TraversalOrderSorter.forProcedureType(procedureType()).sort(transitions()).stream().filter(col -> !col.isEmpty()).reduce((t1, t2) -> t2).orElseGet(List::of);
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

  void accept(Visitor visitor);

  /**
   * Visitor interface for standard {@link Procedure} implementations to allow clients to easily unwrap their own objects or handle
   * ones that Boogie generated after-the-fact.
   */
  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);
  }

  final class Standard implements Procedure {

    private final String procedureIdentifier;

    private final String airportIdentifier;

    private final ProcedureType procedureType;

    private final RequiredNavigationEquipage requiredNavigationEquipage;

    private final Collection<Transition> transitions;

    private int hashCode;

    private Standard(Builder builder) {
      this.procedureIdentifier = requireNonNull(builder.procedureIdentifier);
      this.airportIdentifier = requireNonNull(builder.airportIdentifier);
      this.procedureType = requireNonNull(builder.procedureType);
      this.requiredNavigationEquipage = requireNonNull(builder.requiredNavigationEquipage);
      this.transitions = builder.transitions;
    }

    @Override
    public String procedureIdentifier() {
      return procedureIdentifier;
    }

    @Override
    public String airportIdentifier() {
      return airportIdentifier;
    }

    @Override
    public ProcedureType procedureType() {
      return procedureType;
    }

    @Override
    public RequiredNavigationEquipage requiredNavigationEquipage() {
      return requiredNavigationEquipage;
    }

    @Override
    public Collection<? extends Transition> transitions() {
      return transitions;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Standard standard = (Standard) o;
      return Objects.equals(procedureIdentifier, standard.procedureIdentifier)
          && Objects.equals(airportIdentifier, standard.airportIdentifier)
          && procedureType == standard.procedureType
          && requiredNavigationEquipage == standard.requiredNavigationEquipage
          && Objects.equals(transitions, standard.transitions);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(procedureIdentifier, airportIdentifier, procedureType, requiredNavigationEquipage, transitions);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "procedureIdentifier='" + procedureIdentifier + '\'' +
          ", airportIdentifier='" + airportIdentifier + '\'' +
          ", procedureType=" + procedureType +
          ", requiredNavigationEquipage=" + requiredNavigationEquipage +
          ", transitions=" + transitions +
          '}';
    }

    public static final class Builder {

      private String procedureIdentifier;

      private String airportIdentifier;

      private ProcedureType procedureType;

      private RequiredNavigationEquipage requiredNavigationEquipage;

      private Collection<Transition> transitions;

      private Builder() {
      }

      public Builder procedureIdentifier(String procedureIdentifier) {
        this.procedureIdentifier = requireNonNull(procedureIdentifier);
        return this;
      }

      public Builder airportIdentifier(String airportIdentifier) {
        this.airportIdentifier = requireNonNull(airportIdentifier);
        return this;
      }

      public Builder procedureType(ProcedureType procedureType) {
        this.procedureType = requireNonNull(procedureType);
        return this;
      }

      public Builder requiredNavigationEquipage(RequiredNavigationEquipage requiredNavigationEquipage) {
        this.requiredNavigationEquipage = requireNonNull(requiredNavigationEquipage);
        return this;
      }

      public Builder transitions(Collection<Transition> transitions) {
        this.transitions = transitions;
        return this;
      }

      public Standard build() {
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Procedure {

    private final T datum;

    private final Procedure delegate;

    private int hashCode;

    private Record(T datum, Procedure delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public String procedureIdentifier() {
      return delegate.procedureIdentifier();
    }

    @Override
    public String airportIdentifier() {
      return delegate.airportIdentifier();
    }

    @Override
    public ProcedureType procedureType() {
      return delegate.procedureType();
    }

    @Override
    public RequiredNavigationEquipage requiredNavigationEquipage() {
      return delegate.requiredNavigationEquipage();
    }

    @Override
    public Collection<? extends Transition> transitions() {
      return delegate.transitions();
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Record<?> record = (Record<?>) o;
      return Objects.equals(datum, record.datum)
          && Objects.equals(delegate, record.delegate);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(datum, delegate);
    }

    @Override
    public String toString() {
      return "Record{" +
          "datum=" + datum +
          ", delegate=" + delegate +
          '}';
    }
  }

  final class TransitionMaskedProcedure implements Procedure {

    private final Procedure procedure;

    private final List<Transition> filteredTransitions;

    private TransitionMaskedProcedure(Procedure procedure, Predicate<Transition> shouldMask) {
      this.procedure = requireNonNull(procedure);
      this.filteredTransitions = procedure.transitions().stream().filter(shouldMask.negate()).collect(toList());
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

    @Override
    public void accept(Visitor visitor) {
      procedure.accept(visitor);
    }
  }
}