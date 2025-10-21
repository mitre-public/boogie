package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

import org.mitre.caasd.commons.LatLong;

/**
 * Boogies simplified view of an {@link Helipad}. This is to support route expansion.
 */
public sealed interface Helipad {

  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  static <T> Record<T> record(T datum, Helipad delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Helipad> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * The identifier of the helipad
   * These often end in H.
   *
   * @return a string with the source provided pad number
   */
  String padIdentifier();

  /**
   * The source provided origin for the pad.
   *
   * @return coords
   */
  LatLong origin();

  void accept(Visitor visitor);

  interface Visitor {
    void visit(Standard standard);
    void visit(Record<?> record);
  }

  final class Standard implements Helipad {
    private final String padIdentifier;
    private final LatLong origin;
    private int hashCode;

    public Standard(Builder builder) {
      this.padIdentifier = builder.padIdentifier;
      this.origin = builder.origin;
    }

    @Override
    public String padIdentifier() {
      return padIdentifier;
    }

    @Override
    public LatLong origin() {
      return origin;
    }

    public Builder toBuilder() {
      return Helipad.builder();
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
      return Objects.equals(padIdentifier, standard.padIdentifier) && Objects.equals(origin, standard.origin);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(padIdentifier, origin);
    }

    @Override
    public String toString() {
      return "Standard{" + "padIdentifier='" + padIdentifier + '\'' + ", origin=" + origin + '}';
    }

    public static final class Builder {
      private String padIdentifier;
      private LatLong origin;

      Builder() {
      }

      public Builder padIdentifier(String padIdentifier) {
        this.padIdentifier = padIdentifier;
        return this;
      }

      public Builder origin(LatLong origin) {
        this.origin = origin;
        return this;
      }

      public Helipad build() {
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Helipad {
    private final T datum;
    private final Helipad delegate;
    private int hashCode;

    Record(T datum, Helipad delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public String padIdentifier() {
      return delegate.padIdentifier();
    }

    @Override
    public LatLong origin() {
      return delegate.origin();
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
      return hashCode == record.hashCode && Objects.equals(datum, record.datum) && Objects.equals(delegate, record.delegate);
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
      return "Record{" + "datum=" + datum + ", delegate=" + delegate + ", hashCode=" + hashCode + '}';
    }
  }
}
