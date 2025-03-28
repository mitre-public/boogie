package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;

import org.mitre.caasd.commons.LatLong;

/**
 * An Airspace Sequence from the perspective of Boogie. This class represents a superset of features
 * relevant to all airspace types (enumerated in the {@link Geometry} class).
 *
 * <p>This class provides a collection of factory/builder methods for instantiating sequences of different  {@link Geometry} types
 * with only setters for fields that make sense on them as per the ARINC424 leg specification.
 */
public interface AirspaceSequence {

  static <T> Record<T> record(T datum, AirspaceSequence delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, AirspaceSequence> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  static Standard.Builder builder(@Nonnull Geometry geometry, int sequenceNumber) {
    return new Standard.Builder()
        .geometry(geometry)
        .sequenceNumber(sequenceNumber);
  }

  /**
   * The associated fix for the sequence.
   */
  Optional<LatLong> associatedFix();

  /**
   * For geometries which define arcs as a constant radius turn between fixes this will contain the information about the center
   * of that path.
   * For geometries which define a circle this will contain the center of that circle.
   */
  Optional<LatLong> centerFix();

  /**
   * The radius of the arc or circle in nm
   * @return said radius in nm
   */
  Optional<Double> arcRadius();

  /**
   * Tor ARC/Circle geometry types this is the bearing from the arc center where the leg ends.
   * @return said bearing in degrees
   */
  Optional<Double> arcBearing();

  /**
   * Airspace segments represent a path defined by generic geodesic concepts.
   *
   * @return the geometry of this sequence
   */
  Geometry geometry();

  /**
   * The sequence number of the leg. This represents the position of the sequence in the airspace.
   * <br>
   * Sorting legs by sequence number within airspace should produce the full path as intended by the procedure
   * designer.
   */
  int sequenceNumber();

  void accept(Visitor visitor);

  /**
   * Visitor interface for standard {@link AirspaceSequence} implementations to allow clients to easily unwrap their own objects or handle
   * ones that Boogie generated after-the-fact.
   */
  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);
  }

  final class Standard implements AirspaceSequence {
    private final LatLong associatedFix;
    private final LatLong centerFix;
    private final Double arcRadius;
    private final Double arcBearing;
    private final Geometry geometry;
    private final int sequenceNumber;
    private int hashCode;

    private Standard(Builder builder) {
      this.associatedFix = builder.associatedFix;
      this.centerFix = builder.centerFix;
      this.arcRadius = builder.arcRadius;
      this.arcBearing = builder.arcBearing;
      this.geometry = requireNonNull(builder.geometry);
      this.sequenceNumber = builder.sequenceNumber;
    }

    public Builder toBuilder() {
      return new Builder()
          .associatedFix(associatedFix)
          .centerFix(centerFix)
          .arcRadius(arcRadius)
          .arcBearing(arcBearing)
          .geometry(geometry)
          .sequenceNumber(sequenceNumber);
    }

    @Override
    public Optional<LatLong> associatedFix() {
      return Optional.ofNullable(associatedFix);
    }

    @Override
    public Optional<LatLong> centerFix() {
      return Optional.ofNullable(centerFix);
    }

    @Override
    public Optional<Double> arcRadius() {
      return Optional.ofNullable(arcRadius);
    }

    @Override
    public Optional<Double> arcBearing() {
      return Optional.ofNullable(arcBearing);
    }

    @Override
    public Geometry geometry() {
      return geometry;
    }

    @Override
    public int sequenceNumber() {
      return sequenceNumber;
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
      return sequenceNumber == standard.sequenceNumber && Objects.equals(associatedFix, standard.associatedFix) && Objects.equals(centerFix, standard.centerFix) && Objects.equals(arcRadius, standard.arcRadius) && Objects.equals(arcBearing, standard.arcBearing) && geometry == standard.geometry;
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(associatedFix, centerFix, arcRadius, arcBearing, geometry, sequenceNumber);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "associatedFix=" + associatedFix +
          ", centerFix=" + centerFix +
          ", arcRadius=" + arcRadius +
          ", arcBearing=" + arcBearing +
          ", geometry=" + geometry +
          ", sequenceNumber=" + sequenceNumber +
          '}';
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    public static final class Builder {
      private LatLong associatedFix;
      private LatLong centerFix;
      private Double arcRadius;
      private Double arcBearing;
      private Geometry geometry;
      private int sequenceNumber;

      private Builder() {
      }

      public Builder associatedFix(LatLong associatedFix) {
        this.associatedFix = associatedFix;
        return this;
      }

      public Builder centerFix(LatLong centerFix) {
        this.centerFix = centerFix;
        return this;
      }

      public Builder arcRadius(Double arcRadius) {
        this.arcRadius = arcRadius;
        return this;
      }

      public Builder arcBearing(Double arcBearing) {
        this.arcBearing = arcBearing;
        return this;
      }

      public Builder geometry(Geometry geometry) {
        this.geometry = geometry;
        return this;
      }

      public Builder sequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
      }

      public Standard build() {
        checkArgument(sequenceNumber >= 0, "SequenceNumber should be >= 0.");
        requireNonNull(geometry);
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements AirspaceSequence {
    private final T datum;
    private final AirspaceSequence delegate;
    private int hashCode;

    private Record(T datum, AirspaceSequence delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    @Override
    public Optional<LatLong> associatedFix() {
      return delegate.associatedFix();
    }

    @Override
    public Optional<LatLong> centerFix() {
      return delegate.centerFix();
    }

    @Override
    public Optional<Double> arcRadius() {
      return delegate.arcRadius();
    }

    @Override
    public Optional<Double> arcBearing() {
      return delegate.arcBearing();
    }

    @Override
    public Geometry geometry() {
      return delegate.geometry();
    }

    @Override
    public int sequenceNumber() {
      return delegate.sequenceNumber();
    }

    public T datum() {
      return datum;
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
      return Objects.hash(datum, delegate, hashCode);
    }

    private int computeHashCode() {
      return Objects.hash(datum, delegate, hashCode);
    }

    @Override
    public String toString() {
      return "Record{" +
          "datum=" + datum +
          ", delegate=" + delegate +
          ", hashCode=" + hashCode +
          '}';
    }
  }
}
