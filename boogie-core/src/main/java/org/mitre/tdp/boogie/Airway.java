package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * In Boogie's simplified worldview it is sufficient to represent an airway as a hierarchical piece of navigation infrastructure
 * containing a linear, but bi-directionally flyable, sequence of {@link Leg}s.
 */
public sealed interface Airway {

  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  static <T> Record<T> record(T datum, Airway delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Airway> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * The string identifier of the airway, typically this a 2-5 character identifier, e.g. {@code Y141, J153, J8, V12}.
   */
  String airwayIdentifier();

  /**
   * The ordered sequence of {@link Leg}s contained within the airway.
   *
   * <p>In Boogie these are typically assumed to be flyable bi-directionally.
   */
  List<? extends Leg> legs();

  void accept(Visitor visitor);

  /**
   * Visitor interface for standard {@link Airway} implementations to allow clients to easily unwrap their own objects or handle
   * ones that Boogie generated after-the-fact.
   */
  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);
  }

  final class Standard implements Airway {

    private final String airwayIdentifier;

    private final List<Leg> legs;

    private int hashCode;

    private Standard(Builder builder) {
      this.airwayIdentifier = requireNonNull(builder.airwayIdentifier);
      this.legs = builder.legs;
    }

    @Override
    public String airwayIdentifier() {
      return airwayIdentifier;
    }

    @Override
    public List<Leg> legs() {
      return legs;
    }

    public Builder toBuilder() {
      return new Builder().airwayIdentifier(airwayIdentifier).legs(legs);
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
      return Objects.equals(airwayIdentifier, standard.airwayIdentifier)
          && Objects.equals(legs, standard.legs);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(airwayIdentifier, legs);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "airwayIdentifier='" + airwayIdentifier + '\'' +
          ", legs=" + legs +
          '}';
    }

    public static final class Builder {

      private String airwayIdentifier;

      private List<Leg> legs = new ArrayList<>();

      private Builder() {
      }

      public Builder airwayIdentifier(String airwayIdentifier) {
        this.airwayIdentifier = requireNonNull(airwayIdentifier);
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

  final class Record<T> implements Airway {

    private final T datum;

    private final Airway delegate;

    private int hashCode;

    private Record(T datum, Airway delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public String airwayIdentifier() {
      return delegate.airwayIdentifier();
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
}
