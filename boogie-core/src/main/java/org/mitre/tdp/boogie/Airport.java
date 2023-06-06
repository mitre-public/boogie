package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.LatLong;

/**
 * In a gross simplification, Boogie's worldview of an airport is as a specialized version of a {@link Fix}. This specialization
 * allows various algorithms to use and categorize the two independently where necessary.
 */
public interface Airport extends Fix {

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

  Collection<? extends Runway> runways();

  @Override
  default String fixIdentifier() {
    return airportIdentifier();
  }

  final class Standard implements Airport {

    private final String airportIdentifier;

    private final LatLong latLong;

    private final MagneticVariation magneticVariation;

    private final Collection<Runway> runways;

    private Standard(Builder builder) {
      this.airportIdentifier = requireNonNull(builder.airportIdentifier);
      this.latLong = requireNonNull(builder.latLong);
      this.magneticVariation = builder.magneticVariation;
      this.runways = builder.runways;
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

    public Builder toBuilder() {
      return new Builder()
          .airportIdentifier(airportIdentifier)
          .latLong(latLong)
          .magneticVariation(magneticVariation)
          .runways(runways);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Airport.Standard standard = (Airport.Standard) o;
      return Objects.equals(airportIdentifier, standard.airportIdentifier)
          && Objects.equals(latLong, standard.latLong)
          && Objects.equals(magneticVariation, standard.magneticVariation)
          && Objects.equals(runways, standard.runways);
    }

    @Override
    public int hashCode() {
      return Objects.hash(airportIdentifier, latLong, magneticVariation, runways);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "airportIdentifier='" + airportIdentifier + '\'' +
          ", latLong=" + latLong +
          ", magneticVariation=" + magneticVariation +
          ", runways=" + runways +
          '}';
    }

    public static final class Builder {

      private String airportIdentifier;

      private LatLong latLong;

      private MagneticVariation magneticVariation;

      private Collection<Runway> runways;

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

      public Airport.Standard build() {
        return new Airport.Standard(this);
      }
    }
  }

  final class Record<T> implements Airport {

    private final T datum;

    private final Airport delegate;

    private Record(T datum, Airport delegate) {
      this.datum = requireNonNull(datum);
      this.delegate = requireNonNull(delegate);
    }

    public T datum() {
      return datum;
    }

    @Override
    public String airportIdentifier() {
      return delegate.fixIdentifier();
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
