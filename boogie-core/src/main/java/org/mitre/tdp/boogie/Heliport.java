package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;

/**
 * Boogies simplified view of the {@link  Heliport}. This allows various algs to use and categorize the two independently where necessary.
 */
public interface Heliport extends HasPosition {

  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  static <T> Record<T> record(T datum, Heliport delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Heliport> maker) {
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
  String heliportIdentifier();
  /**
   * The local magnetic variation at the airport - this is typically published with the airport in infrastructure data sources and
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

    private Standard(Builder builder) {
      this.heliportIdentifier = builder.heliportIdentifier;
      this.latLong = builder.latLong;
      this.magneticVariation = builder.magneticVariation;
      this.helipads = builder.helipads;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public String heliportIdentifier() {
      return heliportIdentifier;
    }

    @Override
    public Optional<MagneticVariation> magneticVariation() {
      return Optional.ofNullable(magneticVariation);
    }

    @Override
    public Collection<? extends Helipad> helipads() {
      return Optional.ofNullable(helipads).orElse(List.of());
    }

    @Override
    public LatLong latLong() {
      return latLong;
    }

    public Builder toBuilder() {
      return new Builder()
          .heliportIdentifier(heliportIdentifier)
          .latLong(latLong)
          .magneticVariation(magneticVariation)
          .helipads(helipads);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass())
        return false;
      Standard standard = (Standard) o;
      return Objects.equals(heliportIdentifier, standard.heliportIdentifier) && Objects.equals(latLong, standard.latLong) && Objects.equals(magneticVariation, standard.magneticVariation) && Objects.equals(helipads, standard.helipads);
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

      public Standard build() {
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Heliport {
    private final T datum;
    private final Heliport delegate;
    private int hashCode;

    private Record(T datum, Heliport delegate) {
      this.datum = Objects.requireNonNull(datum);
      this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    public T datum() {
      return datum;
    }

    public Heliport delegate() {
      return delegate;
    }


    @Override
    public String heliportIdentifier() {
      return delegate.heliportIdentifier();
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
    public LatLong latLong() {
      return delegate.latLong();
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

    public int computeHashCode() {
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
