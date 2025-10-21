package org.mitre.tdp.boogie;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;

/**
 * Boogies simplified view of an {@link Heliport}.
 * This allows various algorithms to use and categorize the two independently.
 */
public sealed interface Heliport extends HasPosition {

  /**
   * The given identifier for the heliport, typically users will use the ICAO identifier but any is fine so long as its consistent
   * across the configured infrastructure data objects.
   *
   * <p>Generally speaking, it is up to downstream consumers to standardize these identifiers to FAA, ICAO, etc. whatever standard
   * they prefer. Some downstream modules are sensitive to the values in this field - e.g. the RouteExpander cares whether the
   * identifier here is the same that appears in the route string.
   */
  String heliportIdentifier();
  /**
   * The local magnetic variation at the heliport - this is typically published with the airport in infrastructure data sources and
   * is often the magnetic variation used by the FMS when converting magnetic to true headings/courses in the terminal airspace.
   */
  Optional<MagneticVariation> magneticVariation();
  Collection<? extends Helipad> helipads();

  void accept(Visitor visitor);

  interface Visitor {
    void visit(Standard standard);
    void visit(Record<?> record);
  }

  final class Standard implements Heliport {
    private final String heliportIdentifier;
    private final LatLong latLong;
    private final MagneticVariation magneticVariation;
    private final Collection<Helipad> helipads;
    private int hashCode;

    public Standard(Builder builder) {
      this.heliportIdentifier = builder.heliportIdentifier;
      this.latLong = builder.latLong;
      this.magneticVariation = builder.magneticVariation;
      this.helipads = builder.helipads;
    }

    public Builder toBuilder() {
      return new Builder()
          .heliportIdentifier(heliportIdentifier)
          .latLong(latLong)
          .magneticVariation(magneticVariation)
          .helipads(helipads);
    }

    @Override
    public String heliportIdentifier() {
      return heliportIdentifier;
    }

    @Override
    public LatLong latLong() {
      return latLong;
    }

    @Override
    public Optional<MagneticVariation> magneticVariation() {
      return Optional.ofNullable(magneticVariation);
    }

    @Override
    public Collection<Helipad> helipads() {
      return helipads;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(heliportIdentifier, latLong, magneticVariation, helipads);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass())
        return false;
      Standard standard = (Standard) o;
      return hashCode == standard.hashCode && Objects.equals(heliportIdentifier, standard.heliportIdentifier) && Objects.equals(latLong, standard.latLong) && Objects.equals(magneticVariation, standard.magneticVariation) && Objects.equals(helipads, standard.helipads);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "heliportIdentifier='" + heliportIdentifier + '\'' +
          ", latLong=" + latLong +
          ", magneticVariation=" + magneticVariation +
          ", helipads=" + helipads +
          ", hashCode=" + hashCode +
          '}';
    }

    public static final class Builder {
      private String heliportIdentifier;
      private LatLong latLong;
      private MagneticVariation magneticVariation;
      private Collection<Helipad> helipads;
      private Builder() {}

      public Builder heliportIdentifier(String heliportIdentifier) {
        this.heliportIdentifier = heliportIdentifier;
        return this;
      }

      public Builder latLong(LatLong latLong) {
        this.latLong = latLong;
        return this;
      }

      public Builder magneticVariation(MagneticVariation magneticVariation) {
        this.magneticVariation = magneticVariation;
        return this;
      }

      public Builder helipads(Collection<Helipad> helipads) {
        this.helipads = helipads;
        return this;
      }
    }
  }

  final class Record<T> implements Heliport {
    private final T datum;
    private final Heliport delegate;
    private int hashCode;

    private Record(T datum, Heliport delegate) {
      this.datum = datum;
      this.delegate = delegate;
    }

    public Optional<T> datum() {
      return Optional.ofNullable(datum);
    }

    @Override
    public String heliportIdentifier() {
      return delegate.heliportIdentifier();
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
    public Collection<? extends Helipad> helipads() {
      return delegate.helipads();
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass())
        return false;
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
