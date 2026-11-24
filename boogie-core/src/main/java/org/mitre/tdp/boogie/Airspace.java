package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * An aviation airspace: a lateral boundary over the earth, made of sequences of fixes, arcs, and lines or of a single
 * circle, and a vertical extent between its altitude limits.
 *
 * <p>Implementations are {@link Standard}, a plain value, and {@link Record}, which wraps the source record an airspace
 * was built from around another airspace.
 */
public interface Airspace {

  /**
   * Starts building a {@link Standard} airspace.
   *
   * @return a new builder
   */
  static Standard.Builder builder() {
    return new Standard.Builder();
  }

  /**
   * Wraps an airspace with the source record it was built from.
   *
   * @param datum the source record
   * @param delegate the airspace
   * @param <T> the source record's type
   * @return the airspace, carrying its source record
   */
  static <T> Record<T> record(T datum, Airspace delegate) {
    return new Record<>(datum, delegate);
  }

  /**
   * Builds an airspace from a source record and wraps it with that record.
   *
   * @param datum the source record
   * @param maker builds the airspace from the record
   * @param <T> the source record's type
   * @return the airspace, carrying its source record
   * @throws NullPointerException if the maker is null
   */
  static <T> Record<T> make(T datum, Function<T, Airspace> maker) {
    return new Record<>(datum, requireNonNull(maker, "Should not be null.").apply(datum));
  }

  /**
   * Gets the ARINC 424 customer area code, which namespaces identifiers.
   *
   * @return the area code
   */
  String area();

  /**
   * Gets the identifier for this airspace.
   *
   * @return the identifier
   */
  String identifier();

  /**
   * Gets what type of airspace this is.
   *
   * @return the airspace type
   */
  AirspaceType airspaceType();

  /**
   * Gets the sequences that make up this airspace's lateral boundary, in order.
   *
   * @return the sequences
   */
  List<? extends AirspaceSequence> sequences();

  /**
   * Gets the airspace's vertical extent: its lower and upper limits, each a height measured from its own reference or
   * a value saying that the side has no limit or is not specified.
   *
   * @return the altitude limits
   */
  AltitudeLimits altitudeLimits();

  /**
   * Gets the source identification for the element on which this airspace is based.
   *
   * <p>This is independent of {@link #center()}: a FIR/UIR reference, for example, has identification but no point
   * location. Defaults to empty for implementations that do not retain this metadata.
   *
   * @return the identification, or empty if there is none
   */
  default Optional<CenterIdentification> centerIdentification() {
    return Optional.empty();
  }

  /**
   * Gets the fix the airspace is based on, which may or may not be at its center.
   * <p>
   * Airspaces used to have navaids in the middle, but may now be based on any fix, or on one that is not in the
   * database at all.
   *
   * @return the fix, or empty if there is none
   */
  Optional<? extends Fix> center();

  /**
   * Accepts a visitor, which is called with this airspace's implementation.
   *
   * @param visitor the visitor
   */
  void accept(Visitor visitor);

  /**
   * Visits each {@link Airspace} implementation.
   */
  interface Visitor {

    /**
     * Visits a plain airspace.
     *
     * @param standard the airspace
     */
    void visit(Standard standard);

    /**
     * Visits an airspace carrying its source record.
     *
     * @param record the airspace
     */
    void visit(Record<?> record);
  }

  /**
   * A plain airspace value, built with {@link Airspace#builder()}.
   */
  final class Standard implements Airspace {

    /**
     * The ARINC 424 customer area code.
     */
    private final String area;

    /**
     * The identifier.
     */
    private final String identifier;

    /**
     * The airspace type.
     */
    private final AirspaceType airspaceType;

    /**
     * The sequences that make up the lateral boundary.
     */
    private final List<AirspaceSequence> sequences;

    /**
     * The source identification, or null.
     */
    private final CenterIdentification centerIdentification;

    /**
     * The fix the airspace is based on, or null.
     */
    private final Fix center;

    /**
     * The vertical extent.
     */
    private final AltitudeLimits altitudeLimits;

    /**
     * The cached hash code, or zero until computed.
     */
    private int hashCode;

    /**
     * Creates an airspace from a builder's values.
     *
     * @param builder the builder
     */
    private Standard(Builder builder) {
      this.area = builder.area;
      this.identifier = builder.identifier;
      this.airspaceType = builder.airspaceType;
      this.sequences = builder.sequences;
      this.centerIdentification = builder.centerIdentification;
      this.center = builder.center;
      this.altitudeLimits = builder.altitudeLimits;
    }

    /**
     * Starts a builder with this airspace's values, to make a modified copy.
     *
     * @return a builder holding this airspace's values
     */
    public Builder toBuilder() {
      return new Builder()
          .area(this.area)
          .identifier(this.identifier)
          .airspaceType(this.airspaceType)
          .sequences(this.sequences)
          .centerIdentification(this.centerIdentification)
          .center(this.center)
          .altitudeLimits(this.altitudeLimits);
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
    public Optional<CenterIdentification> centerIdentification() {
      return Optional.ofNullable(centerIdentification);
    }

    @Override
    public Optional<Fix> center() {
      return Optional.ofNullable(center);
    }

    @Override
    public AltitudeLimits altitudeLimits() {
      return altitudeLimits;
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
      return Objects.equals(area, standard.area)
          && Objects.equals(identifier, standard.identifier)
          && airspaceType == standard.airspaceType
          && Objects.equals(sequences, standard.sequences)
          && Objects.equals(centerIdentification, standard.centerIdentification)
          && Objects.equals(center, standard.center)
          && Objects.equals(altitudeLimits, standard.altitudeLimits);
    }

    @Override
    public int hashCode() {
      if (hashCode == 0) {
        this.hashCode = computeHashCode();
      }
      return hashCode;
    }

    /**
     * Computes the hash code from every field.
     *
     * @return the hash code
     */
    private int computeHashCode() {
      return Objects.hash(area, identifier, airspaceType, sequences, centerIdentification, center, altitudeLimits);
    }

    @Override
    public String toString() {
      return "Standard{"
          + "area='" + area + '\''
          + ", identifier='" + identifier + '\''
          + ", airspaceType=" + airspaceType
          + ", sequences=" + sequences
          + ", centerIdentification=" + centerIdentification
          + ", center=" + center
          + ", altitudeLimits=" + altitudeLimits
          + '}';
    }

    /**
     * Builds a {@link Standard} airspace.
     */
    public static final class Builder {

      /**
       * The ARINC 424 customer area code.
       */
      private String area;

      /**
       * The identifier.
       */
      private String identifier;

      /**
       * The airspace type.
       */
      private AirspaceType airspaceType;

      /**
       * The sequences that make up the lateral boundary.
       */
      private List<AirspaceSequence> sequences;

      /**
       * The source identification, or null.
       */
      private CenterIdentification centerIdentification;

      /**
       * The fix the airspace is based on, or null.
       */
      private Fix center;

      /**
       * The vertical extent, not specified until set.
       */
      private AltitudeLimits altitudeLimits = AltitudeLimits.unknown();

      /**
       * Creates an empty builder; see {@link Airspace#builder()}.
       */
      private Builder() {}

      /**
       * Sets the ARINC 424 customer area code.
       *
       * @param area the area code
       * @return this builder
       */
      public Builder area(String area) {
        this.area = area;
        return this;
      }

      /**
       * Sets the identifier.
       *
       * @param identifier the identifier
       * @return this builder
       */
      public Builder identifier(String identifier) {
        this.identifier = identifier;
        return this;
      }

      /**
       * Sets the airspace type.
       *
       * @param airspaceType the airspace type
       * @return this builder
       */
      public Builder airspaceType(AirspaceType airspaceType) {
        this.airspaceType = airspaceType;
        return this;
      }

      /**
       * Sets the sequences that make up the lateral boundary.
       *
       * @param sequences the sequences, in order
       * @return this builder
       */
      public Builder sequences(List<AirspaceSequence> sequences) {
        this.sequences = sequences;
        return this;
      }

      /**
       * Sets the source identification.
       *
       * @param centerIdentification the identification, or null for none
       * @return this builder
       */
      public Builder centerIdentification(CenterIdentification centerIdentification) {
        this.centerIdentification = centerIdentification;
        return this;
      }

      /**
       * Sets the fix the airspace is based on.
       *
       * @param center the fix, or null for none
       * @return this builder
       */
      public Builder center(Fix center) {
        this.center = center;
        return this;
      }

      /**
       * Sets the vertical extent, which otherwise is {@link AltitudeLimits#unknown()}.
       *
       * @param altitudeLimits the limits
       * @return this builder
       * @throws NullPointerException if the limits are null
       */
      public Builder altitudeLimits(AltitudeLimits altitudeLimits) {
        this.altitudeLimits = requireNonNull(altitudeLimits, "altitudeLimits");
        return this;
      }

      /**
       * Builds the airspace.
       *
       * @return the airspace
       */
      public Standard build() {
        return new Standard(this);
      }
    }
  }

  /**
   * An airspace carrying the source record it was built from, delegating everything else to another airspace.
   *
   * @param <T> the source record's type
   */
  final class Record<T> implements Airspace {

    /**
     * The source record.
     */
    private final T datum;

    /**
     * The airspace this one delegates to.
     */
    private final Airspace delegate;

    /**
     * The cached hash code, or zero until computed.
     */
    private int hashCode;

    /**
     * Wraps an airspace with its source record; see {@link Airspace#record(Object, Airspace)}.
     *
     * @param datum the source record
     * @param delegate the airspace
     */
    private Record(T datum, Airspace delegate) {
      this.datum = datum;
      this.delegate = delegate;
    }

    /**
     * Gets the source record.
     *
     * @return the source record
     */
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
    public Optional<CenterIdentification> centerIdentification() {
      return delegate.centerIdentification();
    }

    @Override
    public Optional<? extends Fix> center() {
      return delegate.center();
    }

    @Override
    public AltitudeLimits altitudeLimits() {
      return delegate.altitudeLimits();
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

    /**
     * Computes the hash code from the source record and the airspace.
     *
     * @return the hash code
     */
    private int computeHashCode() {
      return Objects.hash(datum, delegate);
    }

    @Override
    public String toString() {
      return "Record{"
          + "datum=" + datum
          + ", delegate=" + delegate
          + '}';
    }
  }
}
