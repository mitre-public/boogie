package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;

/**
 * Boogie's simplified view of an {@link Airport} is  This specialization
 * allows various algorithms to use and categorize the two independently where necessary.
 */
public interface Airport extends HasPosition {

  static Standard.Builder builder() {
    return new Airport.Standard.Builder();
  }

  static <T> Record<T> record(T datum, Airport delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Airport> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * The given identifier for the airport, typically users will use the ICAO identifier but any is fine so long as its consistent
   * across the configured infrastructure data objects.
   *
   * <p>Generally speaking it is up to downstream consumers to standardize these identifiers to FAA, ICAO, etc. whatever standard
   * they prefer. Some downstream modules are sensitive to the values in this field - e.g. the RouteExpander cares whether the
   * identifier here is the same that appears in the route string.
   */
  String airportIdentifier();

  /**
   * The local magnetic variation at the airport - this is typically published with the airport in infrastructure data sources and
   * is often the magnetic variation used by the FMS when converting magnetic to true headings/courses in the terminal airspace.
   */
  Optional<MagneticVariation> magneticVariation();

  Collection<? extends Runway> runways();

  Collection<? extends Helipad> helipads();

  void accept(Visitor visitor);

  /**
   * Visitor interface for standard {@link Airport} implementations to allow clients to easily unwrap their own objects or handle
   * ones that Boogie generated after-the-fact.
   */
  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);
  }

  final class Standard implements Airport {

    private final String airportIdentifier;

    private final LatLong latLong;

    private final MagneticVariation magneticVariation;

    private final Collection<Runway> runways;

    private final Collection<Helipad> helipads;

    private int hashCode;

    private Standard(Builder builder) {
      this.airportIdentifier = requireNonNull(builder.airportIdentifier);
      this.latLong = requireNonNull(builder.latLong);
      this.magneticVariation = builder.magneticVariation;
      this.runways = builder.runways;
      this.helipads = builder.helipads;
    }

    @Override
    public String airportIdentifier() {
      return airportIdentifier;
    }

    @Override
    public Optional<MagneticVariation> magneticVariation() {
      return ofNullable(magneticVariation);
    }

    @Override
    public LatLong latLong() {
      return latLong;
    }

    @Override
    public Collection<Runway> runways() {
      return runways;
    }

    @Override
    public Collection<Helipad> helipads() {
      return helipads;
    }

    public Builder toBuilder() {
      return new Builder()
          .airportIdentifier(airportIdentifier)
          .latLong(latLong)
          .magneticVariation(magneticVariation)
          .runways(runways)
          .helipads(helipads);
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
      return Objects.equals(airportIdentifier, standard.airportIdentifier)
          && Objects.equals(latLong, standard.latLong)
          && Objects.equals(magneticVariation, standard.magneticVariation)
          && Objects.equals(runways, standard.runways)
          && Objects.equals(helipads, standard.helipads);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    // used by EqualsVerifier
    private int computeHashCode() {
      return Objects.hash(airportIdentifier, latLong, magneticVariation, runways, helipads);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "airportIdentifier='" + airportIdentifier + '\'' +
          ", latLong=" + latLong +
          ", magneticVariation=" + magneticVariation +
          ", runways=" + runways +
          ", helipads=" + helipads +
          '}';
    }

    public static final class Builder {

      private String airportIdentifier;

      private LatLong latLong;

      private MagneticVariation magneticVariation;

      private Collection<Runway> runways;

      private Collection<Helipad> helipads;

      private Builder() {
      }

      public Builder airportIdentifier(String airportIdentifier) {
        this.airportIdentifier = requireNonNull(airportIdentifier);
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

      /**
       * Override the current set of configured runways to be the provided collection.
       */
      public Builder runways(Collection<? extends Runway> runways) {
        this.runways = new ArrayList<>(runways);
        return this;
      }

      /**
       * Append a new leg to the end of the current collection of runways.
       */
      public Builder add(Runway runway) {
        this.runways.add(runway);
        return this;
      }

      /**
       * Provide a list of helipads
       * @param helipads this list will be copied into a new list
       * @return the builder
       */
      public Builder helipads(Collection<? extends Helipad> helipads) {
        this.helipads = new ArrayList<>(helipads);
        return this;
      }

      /**
       * Add a helipad to the list in the builder
       * @param helipad to add
       * @return the builder
       */
      public Builder add(Helipad helipad) {
        this.helipads.add(helipad);
        return this;
      }

      public Airport.Standard build() {
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Airport {

    private final T datum;

    private final Airport delegate;

    private int hashCode;

    private Record(T datum, Airport delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public String airportIdentifier() {
      return delegate.airportIdentifier();
    }

    @Override
    public Optional<MagneticVariation> magneticVariation() {
      return delegate.magneticVariation();
    }

    @Override
    public LatLong latLong() {
      return delegate.latLong();
    }

    @Override
    public Collection<? extends Runway> runways() {
      return delegate.runways();
    }

    @Override
    public Collection<? extends Helipad> helipads() {
      return delegate.helipads();
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
      Airport.Record<?> record = (Airport.Record<?>) o;
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
