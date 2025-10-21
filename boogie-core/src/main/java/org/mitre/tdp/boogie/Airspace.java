package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.collect.Range;

/**
 * This is a generic way to hold aviation airspace concepts. They should form a shape over the earth or
 * just have one sequence and be a circle.
 */
public sealed interface Airspace {

  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  static <T> Record<T> record(T datum, Airspace delegate) {
    return new Record<>(datum, delegate);
  }

  static <T> Record<T> make(T datum, Function<T, Airspace> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * This is the ARINC 424 area code, needed for namespaces.
   */
  String area();

  /**
   * The identifier for this airspace
   * @return the string that identifies this airspace
   */
  String identifier();

  /**
   * What type of airspace this is
   * @return the list
   */
  AirspaceType airspaceType();

  /**
   * The list of sequences in this airspace
   * @return the list
   */
  List<? extends AirspaceSequence> sequences();

  /**
   * A segment of airspace only applies to a certain range of altitude in MSL
   *
   * @return the range of doubles
   */
  Range<Double> altitudeLimit();

  /**
   * This is a fix that the airspace is based on, which might actually be its center or not.
   * They used to have navaids in the middle, but now could be whatever or not even actually a fix in the database.
   * @return the name of the center.
   */
  Optional<String> centerIdent();
  /**
   * This is a fix that the airspace is based on, which might actually be its center or not.
   * They used to have navaids in the middle, but now could be whatever fix, or not even actually a fix in the database.
   * @return a fix which might exist
   */
  Optional<? extends Fix> center();

  void accept(Visitor visitor);

  interface Visitor {

    void visit(Standard standard);

    void visit(Record<?> record);
  }

  final class Standard implements Airspace {
    private final String area;
    private final String identifier;
    private final AirspaceType airspaceType;
    private final List<AirspaceSequence> sequences;
    private final String centerIdent;
    private final Fix center;
    private final Range<Double> altitudeLimit;
    private int hashCode;

    private Standard(Builder builder) {
      this.area = builder.area;
      this.identifier = builder.identifier;
      this.airspaceType = builder.airspaceType;
      this.sequences = builder.sequences;
      this.centerIdent = builder.centerIdent;
      this.center = builder.center;
      this.altitudeLimit = builder.altitudeLimit;
    }

    public Builder toBuilder() {
      return new Builder()
          .area(this.area)
          .identifier(this.identifier)
          .airspaceType(this.airspaceType)
          .sequences(this.sequences)
          .centerIdent(this.centerIdent)
          .center(this.center)
          .altitudeLimit(this.altitudeLimit);
    }

    @Override
    public String area() {
      return area;
    }

    @Override
    public String identifier() {
      return identifier;
    }

    @Override
    public AirspaceType airspaceType() {
      return airspaceType;
    }

    @Override
    public List<? extends AirspaceSequence> sequences() {
      return sequences;
    }

    @Override
    public Optional<String> centerIdent() {
      return Optional.ofNullable(centerIdent);
    }

    @Override
    public Optional<Fix> center() {
      return Optional.ofNullable(center);
    }

    @Override
    public Range<Double> altitudeLimit() {
      return altitudeLimit;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      Standard standard = (Standard) o;
      return Objects.equals(area, standard.area) && Objects.equals(identifier, standard.identifier) && airspaceType == standard.airspaceType && Objects.equals(sequences, standard.sequences) && Objects.equals(centerIdent, standard.centerIdent) && Objects.equals(center, standard.center) && Objects.equals(altitudeLimit, standard.altitudeLimit);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    private int computeHashCode() {
      return Objects.hash(area, identifier, airspaceType, sequences, centerIdent, center, altitudeLimit);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "area='" + area + '\'' +
          "identifier='" + identifier + '\'' +
          ", airspaceType=" + airspaceType +
          ", sequences=" + sequences +
          ", centerIdent='" + centerIdent + '\'' +
          ", center=" + center +
          ", altitudeLimit=" + altitudeLimit +
          '}';
    }

    public static final class Builder {
      private String area;
      private String identifier;
      private AirspaceType airspaceType;
      private List<AirspaceSequence> sequences;
      private String centerIdent;
      private Fix center;
      private Range<Double> altitudeLimit;

      private Builder() {}

      public Builder area(String area) {
        this.area = area;
        return this;
      }

      public Builder identifier(String identifier) {
        this.identifier = identifier;
        return this;
      }

      public Builder airspaceType(AirspaceType airspaceType) {
        this.airspaceType = airspaceType;
        return this;
      }

      public Builder sequences(List<AirspaceSequence> sequences) {
        this.sequences = sequences;
        return this;
      }

      public Builder centerIdent(String centerIdent) {
        this.centerIdent = centerIdent;
        return this;
      }

      public Builder center(Fix center) {
        this.center = center;
        return this;
      }

      public Builder altitudeLimit(Range<Double> altitudeLimit) {
        this.altitudeLimit = altitudeLimit;
        return this;
      }

      public Standard build() {
        return new Standard(this);
      }
    }
  }

  final class Record<T> implements Airspace {
    private final T datum;

    private final Airspace delegate;

    private int hashCode;

    private Record(T datum, Airspace delegate) {
      this.datum = datum;
      this.delegate = delegate;
    }

    public T datum() {
      return datum;
    }

    @Override
    public String area() {
      return delegate.area();
    }

    @Override
    public String identifier() {
      return delegate.identifier();
    }

    @Override
    public AirspaceType airspaceType() {
      return delegate.airspaceType();
    }

    @Override
    public List<? extends AirspaceSequence> sequences() {
      return delegate.sequences();
    }

    @Override
    public Optional<String> centerIdent() {
      return delegate.centerIdent();
    }

    @Override
    public Optional<? extends Fix> center() {
      return delegate.center();
    }

    @Override
    public Range<Double> altitudeLimit() {
      return delegate.altitudeLimit();
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
