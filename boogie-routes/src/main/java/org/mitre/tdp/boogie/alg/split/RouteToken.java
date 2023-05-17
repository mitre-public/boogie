package org.mitre.tdp.boogie.alg.split;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.resolve.infer.SectionInferrer;

/**
 * Represents an indexed portion of a route string which has been extracted as a token (potentially with some standardization
 * applied) and converted to a value which can be handed off to {@link SectionResolver}s for resolution.
 *
 * <p>These tokens need to be sequentially ordered so downstream consumers of them can traverse them in the order they appeared
 * in the original route string (if necessary).
 *
 * <p>This interface is provided to allow various {@link RouteTokenizer} implementations to resolve tokens which contain more
 * than the required information for expansion to run (e.g. requested altitude and speed, wildcard characters, etc). Tokens can
 * then be visited by {@link RouteTokenVisitor} implementations to collect this information.
 */
public interface RouteToken {

  /**
   * Returns a new "standard" implementation of a route token which only contains the interface values.
   */
  static RouteToken standard(String value, double index) {
    return new Standard(value, index);
  }

  /**
   * Returns a new builder for a FAA IFR formatted route token (including any supported special characters/qualifiers).
   */
  static FaaIfr.Builder faaIfrBuilder(String value, double index) {
    return new FaaIfr.Builder(new Standard(value, index));
  }

  /**
   * Returns a new builder for a ICAO formatted route token, including support for additional specification such as speed/altitude
   * requests and IFR/VFR status.
   */
  static Icao.Builder icaoBuilder(String value, double index) {
    return new Icao.Builder(new Standard(value, index));
  }

  /**
   * Create a new {@link RouteToken} with the given identifier between the provided left and right tokens. This method primarily
   * supports the various {@link SectionInferrer} implementations.
   */
  static RouteToken between(String infrastructureName, RouteToken left, RouteToken right) {
    return standard(infrastructureName, (left.index() + right.index()) / 2.);
  }

  /**
   * The name of the referenced infrastructure element extracted from the underlying route string.
   *
   * <p>This should a matchable identifier for the piece of navigation infrastructure referred to by this token in the route. In
   * general this will be something like a procedure name but may also be a Lat/Lon literal or something else.
   *
   * <p>It is this method which is used by {@link SectionResolver} implementations to resolve underlying infrastructure info. If
   * alternate token types need to be processed a new resolver implementation may need to be created and configured.
   */
  String infrastructureName();

  /**
   * Index of the token in the original route string. As tokens may be created in the {@link SectionInferrer} process this field
   * is double-valued.
   *
   * <p>The general contract is tokens from the route literal should be integer-valued while "inferred" sections have fractional
   * values.
   */
  double index();

  /**
   * Default behavior for {@link RouteToken} implementations which don't accept visitors which may want to inspect their content.
   */
  default void accept(RouteTokenVisitor visitor) {
    // hello goodbye
  }

  final class Standard implements RouteToken {

    private final String value;

    private final double index;

    private Standard(String value, double index) {
      this.value = requireNonNull(value);
      this.index = index;
    }

    @Override
    public String infrastructureName() {
      return value;
    }

    @Override
    public double index() {
      return index;
    }

    @Override
    public void accept(RouteTokenVisitor visitor) {
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
      return Double.compare(standard.index, index) == 0 && Objects.equals(value, standard.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, index);
    }

    @Override
    public String toString() {
      return "Standard{" +
          "value='" + value + '\'' +
          ", index=" + index +
          '}';
    }
  }

  final class FaaIfr implements RouteToken {

    private final Standard delegate;

    private final String etaEet;

    private final String wildcards;

    private FaaIfr(Builder builder) {
      this.delegate = requireNonNull(builder.delegate);
      this.etaEet = builder.etaEet;
      this.wildcards = builder.wildcards;
    }

    @Override
    public String infrastructureName() {
      return delegate.infrastructureName();
    }

    public Optional<String> etaEet() {
      return ofNullable(etaEet);
    }

    public Optional<String> wildcards() {
      return ofNullable(wildcards);
    }

    @Override
    public double index() {
      return delegate.index();
    }

    @Override
    public void accept(RouteTokenVisitor visitor) {
      visitor.visit(this);
    }

    public Builder toBuilder() {
      return new Builder(delegate).etaEet(etaEet).wildcards(wildcards);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      FaaIfr faaIfr = (FaaIfr) o;
      return Objects.equals(delegate, faaIfr.delegate)
          && Objects.equals(etaEet, faaIfr.etaEet)
          && Objects.equals(wildcards, faaIfr.wildcards);
    }

    @Override
    public int hashCode() {
      return Objects.hash(delegate, etaEet, wildcards);
    }

    @Override
    public String toString() {
      return "FaaIfr{" +
          "delegate=" + delegate +
          ", etaEet='" + etaEet + '\'' +
          ", wildcards='" + wildcards + '\'' +
          '}';
    }

    public static final class Builder {

      private final Standard delegate;

      private String etaEet;

      private String wildcards;

      private Builder(Standard delegate) {
        this.delegate = requireNonNull(delegate);
      }

      public Builder etaEet(String etaEet) {
        this.etaEet = etaEet;
        return this;
      }

      public Builder wildcards(String wildcards) {
        this.wildcards = wildcards;
        return this;
      }

      public FaaIfr build() {
        return new FaaIfr(this);
      }
    }
  }

  final class Icao implements RouteToken {

    private final Standard delegate;

    private final String etaEet;

    private final String wildcards;

    private final String speedLevel;

    private final String flightRules;

    private Icao(Builder builder) {
      this.delegate = requireNonNull(builder.delegate);
      this.etaEet = builder.etaEet;
      this.wildcards = builder.wildcards;
      this.speedLevel = builder.speedLevel;
      this.flightRules = builder.flightRules;
    }

    @Override
    public String infrastructureName() {
      return delegate.infrastructureName();
    }

    @Override
    public double index() {
      return delegate.index();
    }

    public Optional<String> etaEet() {
      return ofNullable(etaEet);
    }

    public Optional<String> wildcards() {
      return ofNullable(wildcards);
    }

    public Optional<String> speedLevel() {
      return ofNullable(speedLevel);
    }

    public Optional<String> flightRules() {
      return ofNullable(flightRules);
    }

    @Override
    public void accept(RouteTokenVisitor visitor) {
      visitor.visit(this);
    }

    public Builder toBuilder() {
      return new Builder(delegate).etaEet(etaEet).wildcards(wildcards).speedLevel(speedLevel).flightRules(flightRules);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Icao icao = (Icao) o;
      return Objects.equals(delegate, icao.delegate) && Objects.equals(etaEet, icao.etaEet) && Objects.equals(wildcards, icao.wildcards) && Objects.equals(speedLevel, icao.speedLevel) && Objects.equals(flightRules, icao.flightRules);
    }

    @Override
    public int hashCode() {
      return Objects.hash(delegate, etaEet, wildcards, speedLevel, flightRules);
    }

    @Override
    public String toString() {
      return "Icao{" +
          "delegate=" + delegate +
          ", etaEet='" + etaEet + '\'' +
          ", wildcards='" + wildcards + '\'' +
          ", speedLevel='" + speedLevel + '\'' +
          ", flightRules='" + flightRules + '\'' +
          '}';
    }

    public static final class Builder {

      private final Standard delegate;

      private String etaEet;

      private String wildcards;

      private String speedLevel;

      private String flightRules;

      private Builder(Standard delegate) {
        this.delegate = requireNonNull(delegate);
      }

      public Builder etaEet(String etaEet) {
        this.etaEet = etaEet;
        return this;
      }

      public Builder wildcards(String wildcards) {
        this.wildcards = wildcards;
        return this;
      }

      public Builder speedLevel(String speedLevel) {
        this.speedLevel = speedLevel;
        return this;
      }

      public Builder flightRules(String flightRules) {
        this.flightRules = flightRules;
        return this;
      }

      public Icao build() {
        return new Icao(this);
      }
    }
  }
}
