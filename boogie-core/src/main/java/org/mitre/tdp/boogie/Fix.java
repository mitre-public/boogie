package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;

/**
 * In Boogie's simplified worldview a fix is any named reference point in space (this could be a VOR, NDB, waypoint, etc.) and is
 * explicitly meant to be fairly flexible.
 *
 * <p>While all the above entities can define a lot of additional information the algorithms provided by Boogie don't required
 * access to information beyond what's specified here.
 *
 * <p>The inclusion of a magnetic variation (published or modeled) is to allow Boogie to convert from magnetic to true courses in
 * reference to these objects for both conformance computations (e.g. to VA, VA, etc. legs) and to approximate projected locations
 * of referenced FRDs.
 */
public sealed interface Fix extends HasPosition {

  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  static <T> Record<T> record(T datum, Fix delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Fix> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * Special-case representation of a fix where the position is determined by projecting a distance along a certain radial from an
   * existing fix.
   *
   * @param origin   the fix representing the origin of the projection
   * @param bearing  the bearing to project along from the fix
   * @param distance the distance to project along the bearing
   */
  static FixRadialDistance fixRadialDistance(Fix origin, Course bearing, Distance distance) {
    return new FixRadialDistance(origin, bearing, distance);
  }

  /**
   * The (typically) 3-5 character name of the fix object e.g. {@code JMACK, CHPPR, DTW}.
   */
  String fixIdentifier();

  /**
   * The local magnetic variation at the fix - typically this will be published if the fix is a navaid (e.g. VOR/NDB) but won't
   * be for waypoints (as there is no physical facility there).
   *
   * <p>In the latter case (since they're still used as reference points for FRDs in some instances) it can either be left empty,
   * set to zero, or explicitly modeled using the provided {@link Declinations} class.
   *
   * <p>Where Boogie algorithms need this value internally it will be implicitly derived via the {@link Declinations} class under
   * the hood. Given there is a time component to the variation there is no guarantee this choice will be accurate, so it's best
   * to provide this value yourself where possible.
   */
  Optional<MagneticVariation> magneticVariation();

  void accept(Visitor visitor);

  /**
   * Visitor interface for standard {@link Fix} implementations to allow clients to easily unwrap their own objects or handle
   * ones that Boogie generated after-the-fact.
   */
  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);

    void visit(FixRadialDistance frd);
  }

  final class Standard implements Fix {

    private final String fixIdentifier;

    private final LatLong latLong;

    private final MagneticVariation magneticVariation;

    private int hashCode;

    private Standard(Builder builder) {
      this.fixIdentifier = requireNonNull(builder.fixIdentifier);
      this.latLong = requireNonNull(builder.latLong);
      this.magneticVariation = builder.magneticVariation;
    }

    @Override
    public String fixIdentifier() {
      return fixIdentifier;
    }

    @Override
    public LatLong latLong() {
      return latLong;
    }

    @Override
    public Optional<MagneticVariation> magneticVariation() {
      return ofNullable(magneticVariation);
    }

    public Builder toBuilder() {
      return new Builder()
          .fixIdentifier(fixIdentifier)
          .latLong(latLong)
          .magneticVariation(magneticVariation);
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
      return Objects.equals(fixIdentifier, standard.fixIdentifier)
          && Objects.equals(latLong, standard.latLong)
          && Objects.equals(magneticVariation, standard.magneticVariation);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(fixIdentifier, latLong, magneticVariation);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "fixIdentifier='" + fixIdentifier + '\'' +
          ", latLong=" + latLong +
          ", magneticVariation=" + magneticVariation +
          '}';
    }

    public static final class Builder {

      private String fixIdentifier;

      private LatLong latLong;

      private MagneticVariation magneticVariation;

      private Builder() {
      }

      public Builder fixIdentifier(String fixIdentifier) {
        this.fixIdentifier = requireNonNull(fixIdentifier);
        return this;
      }

      public Builder latLong(LatLong latLong) {
        this.latLong = requireNonNull(latLong);
        return this;
      }

      public Builder magneticVariation(MagneticVariation magneticVariation) {
        this.magneticVariation = magneticVariation;
        return this;
      }

      public Standard build() {
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Fix {

    private final T datum;

    private final Fix delegate;

    private int hashCode;

    private Record(T datum, Fix delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public String fixIdentifier() {
      return delegate.fixIdentifier();
    }

    @Override
    public LatLong latLong() {
      return delegate.latLong();
    }

    @Override
    public Optional<MagneticVariation> magneticVariation() {
      return delegate.magneticVariation();
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

  final class FixRadialDistance implements Fix {

    private final Fix origin;

    private final Course radial;

    private final Distance distance;

    private final LatLong projectedLocation;

    private FixRadialDistance(Fix origin, Course radial, Distance distance) {
      this.origin = requireNonNull(origin);
      this.radial = requireNonNull(radial);
      this.distance = requireNonNull(distance);
      this.projectedLocation = makeProjectedLocation(origin, radial, distance);
    }

    private static LatLong makeProjectedLocation(Fix origin, Course radial, Distance distance) {

      MagneticVariation variation = origin.magneticVariation()
          .orElseGet(() -> MagneticVariation.ofDegrees(Declinations.approx(origin.latitude(), origin.longitude())));

      return origin.latLong().project(variation.magneticToTrue(radial), distance);
    }

    @Override
    public String fixIdentifier() {
      return String.format("%s%03d%03d",
          origin.fixIdentifier(),
          Double.valueOf(radial.inDegrees()).intValue(),
          Double.valueOf(distance.inNauticalMiles()).intValue()
      );
    }

    @Override
    public Optional<MagneticVariation> magneticVariation() {
      return origin.magneticVariation();
    }

    @Override
    public LatLong latLong() {
      return projectedLocation;
    }

    public Fix fix() {
      return origin;
    }

    public Course radial() {
      return radial;
    }

    public Distance distance() {
      return distance;
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
      FixRadialDistance that = (FixRadialDistance) o;
      return Objects.equals(origin, that.origin)
          && Objects.equals(radial, that.radial)
          && Objects.equals(distance, that.distance)
          && Objects.equals(projectedLocation, that.projectedLocation);
    }

    @Override
    public int hashCode() {
      return Objects.hash(origin, radial, distance, projectedLocation);
    }

    @Override
    public String toString() {
      return "FixRadialDistance{" +
          "origin=" + origin +
          ", radial=" + radial +
          ", distance=" + distance +
          ", projectedLocation=" + projectedLocation +
          '}';
    }
  }
}