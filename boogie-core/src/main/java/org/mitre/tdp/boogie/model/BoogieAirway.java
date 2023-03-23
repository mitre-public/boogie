package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;

/**
 * Immutable, buildable implementation of the {@link Airway} interface provided for convenience.
 */
public final class BoogieAirway implements Airway {

  private final String airwayIdentifier;
  private final String airwayRegion;
  private final List<BoogieLeg> legs;

  private BoogieAirway(Builder builder) {
    this.airwayIdentifier = builder.airwayIdentifier;
    this.airwayRegion = builder.airwayRegion;
    this.legs = builder.legs;
  }

  @Override
  public String airwayIdentifier() {
    return airwayIdentifier;
  }

  @Override
  public String airwayRegion() {
    return airwayRegion;
  }

  @Override
  public List<BoogieLeg> legs() {
    return legs;
  }

  public Builder toBuilder() {
    return new Builder()
        .airwayIdentifier(airwayIdentifier())
        .airwayRegion(airwayRegion())
        .legs(legs());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoogieAirway that = (BoogieAirway) o;
    return Objects.equals(airwayIdentifier, that.airwayIdentifier) &&
        Objects.equals(airwayRegion, that.airwayRegion) &&
        Objects.equals(legs, that.legs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwayIdentifier, airwayRegion, legs);
  }

  @Override
  public String toString() {
    return "BoogieAirway{" +
        "airwayIdentifier='" + airwayIdentifier + '\'' +
        ", airwayRegion='" + airwayRegion + '\'' +
        ", legs=" + legs +
        '}';
  }

  public static final class Builder {
    private String airwayIdentifier;
    private String airwayRegion;
    private List<BoogieLeg> legs;

    public Builder airwayIdentifier(String airwayIdentifier) {
      this.airwayIdentifier = requireNonNull(airwayIdentifier);
      return this;
    }

    public Builder airwayRegion(String airwayRegion) {
      this.airwayRegion = requireNonNull(airwayRegion);
      return this;
    }

    public Builder legs(List<BoogieLeg> legs) {
      this.legs = legs;
      return this;
    }

    public BoogieAirway build() {
      return new BoogieAirway(this);
    }
  }
}
