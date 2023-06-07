package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Transitions represent logical <i>linear</i> sequences of legs within a {@link Procedure}.
 *
 * <p>Multiple transitions together represent the collection of linear paths through a procedure and can be zipped together into
 * a DAG representing the overall procedure 'graph'. In the cases of SIDs/STARs this typically works out as:
 * <ol>
 *   <li>SIDs: The end fix of the runway transitions are the first fix of the common portion - then the end fix of the common portion
 *   is the start fix of the enroute transitions.</li>
 *   <li>STARs: The end fix of the enroute transitions is the first fix of the common portion - then the end fix of the common portion
 *   is the start fix of the runway transitions.</li>
 * </ol>
 *
 * <p>Approach procedures operate somewhat similarly, but don't have runway transitions. They have "approach" transitions which
 * typically either directly connect to a STAR or serve to collect traffic of the end of a STAR/from terminal airspace onto the
 * final straight-in common portion of the approach.
 *
 * <p>Optionally most approach procedures will contain a MISSED transition which can be flown when the aircraft initiates a go-around
 * due to something that occurred during the approach.
 *
 * <p>Often MISSED transitions will end in a leg that indicates it's associated fix is {@link Leg#isPublishedHoldingFix()} - so
 * the aircraft can be held before being re-inserted into the arrival sequencing at the airport.
 */
public interface Transition {

  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  static <T> Record<T> record(T datum, Transition delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Transition> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * The optional name of the transition. Boogie is mostly dealing with transitions encapsulated in {@link Procedure}s and isn't
   * doing any special processing on these, most algorithms will declare they specifically require assembled procedures up-front.
   *
   * <p>Transition naming conventions are typically to the effect of:
   * <ol>
   *   <li>{@link TransitionType#COMMON} - these are generally either null or named something like "ALL" as all traffic which traverses
   *   the SID/STAR/APPROACH must pass through this transition. In some cases though a procedure won't have a common portion, in which
   *   case there will be a common merge fix (shared by the enroute/runway transitions) - but not a clearly defined COMMON transition.</li>
   *   <li>{@link TransitionType#ENROUTE} - these are typically named for their entry fix (in the STAR case) or for their exit fix (in
   *   the SID case). E.g. an ENROUTE STAR transition who's first fix is STRDR is likely to be named similarly... i.e. STRDR.</li>
   *   <li>{@link TransitionType#RUNWAY} - these are generally named after the runway (or runways) they service. E.g. a transition
   *   serving a single runway may be named 'RW13R' or one servicing two (a left/right) may be called 'RW23B'.</li>
   * </ol>
   */
  Optional<String> transitionIdentifier();

  /**
   * See {@link TransitionType} - used by Boogie to sequence transitions by intended traversal order within a {@link Procedure}.
   */
  TransitionType transitionType();

  /**
   * Returns the ordered sequence of legs which make up the transition. In general Boogie ignores the {@link Leg#sequenceNumber()}
   * and exclusively relies on the ordering of legs provided here.
   */
  List<? extends Leg> legs();

  void accept(Visitor visitor);

  /**
   * Visitor interface for standard {@link Transition} implementations to allow clients to easily unwrap their own objects or handle
   * ones that Boogie generated after-the-fact.
   */
  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);
  }

  final class Standard implements Transition {

    private final String transitionIdentifier;

    private final TransitionType transitionType;

    private final List<Leg> legs;

    private Standard(Builder builder) {
      this.transitionIdentifier = builder.transitionIdentifier;
      this.transitionType = requireNonNull(builder.transitionType);
      this.legs = builder.legs;
    }

    @Override
    public Optional<String> transitionIdentifier() {
      return ofNullable(transitionIdentifier);
    }

    @Override
    public TransitionType transitionType() {
      return transitionType;
    }

    @Override
    public List<Leg> legs() {
      return legs;
    }

    public Builder toBuilder() {
      return new Builder()
          .transitionIdentifier(transitionIdentifier)
          .transitionType(transitionType)
          .legs(legs);
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
      return Objects.equals(transitionIdentifier, standard.transitionIdentifier)
          && transitionType == standard.transitionType
          && Objects.equals(legs, standard.legs);
    }

    @Override
    public int hashCode() {
      return Objects.hash(transitionIdentifier, transitionType, legs);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "transitionIdentifier='" + transitionIdentifier + '\'' +
          ", transitionType=" + transitionType +
          ", legs=" + legs +
          '}';
    }

    public static final class Builder {

      private String transitionIdentifier;

      private TransitionType transitionType;

      private List<Leg> legs = new ArrayList<>();

      private Builder() {
      }

      public Builder transitionIdentifier(String transitionIdentifier) {
        this.transitionIdentifier = transitionIdentifier;
        return this;
      }

      public Builder transitionType(TransitionType transitionType) {
        this.transitionType = requireNonNull(transitionType);
        return this;
      }

      /**
       * Override the current set of configured legs to be the provided list.
       */
      public Builder legs(List<? extends Leg> legs) {
        this.legs = new ArrayList<>(legs);
        return this;
      }

      /**
       * Append a new leg to the end of the current list of configured legs.
       */
      public Builder add(Leg leg) {
        this.legs.add(leg);
        return this;
      }

      public Standard build() {
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Transition {

    private final T datum;

    private final Transition delegate;

    private Record(T datum, Transition delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public Optional<String> transitionIdentifier() {
      return delegate.transitionIdentifier();
    }

    @Override
    public TransitionType transitionType() {
      return delegate.transitionType();
    }

    @Override
    public List<? extends Leg> legs() {
      return delegate.legs();
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
}
