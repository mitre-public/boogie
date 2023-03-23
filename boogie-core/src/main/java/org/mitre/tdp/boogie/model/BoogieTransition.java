package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

/**
 * Immutable, buildable implementation of the {@link Transition} interface provided for convenience.
 */
public final class BoogieTransition implements Transition {

  private final String transitionIdentifier;
  private final String procedureIdentifier;
  private final String airportIdentifier;
  private final String airportRegion;
  private final ProcedureType procedureType;
  private final TransitionType transitionType;
  private final List<BoogieLeg> legs;

  private BoogieTransition(Builder builder) {
    this.transitionIdentifier = builder.transitionIdentifier;
    this.procedureIdentifier = builder.procedureIdentifier;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportRegion = builder.airportRegion;
    this.procedureType = builder.procedureType;
    this.transitionType = builder.transitionType;
    this.legs = builder.legs;
  }

  @Override
  public Optional<String> transitionIdentifier() {
    return Optional.ofNullable(transitionIdentifier);
  }

  @Override
  public String procedureIdentifier() {
    return procedureIdentifier;
  }

  @Override
  public String airportIdentifier() {
    return airportIdentifier;
  }

  @Override
  public String airportRegion() {
    return airportRegion;
  }

  @Override
  public ProcedureType procedureType() {
    return procedureType;
  }

  @Override
  public TransitionType transitionType() {
    return transitionType;
  }

  @Override
  public List<BoogieLeg> legs() {
    return legs;
  }

  public Builder toBuilder() {
    return new Builder()
        .transitionIdentifier(transitionIdentifier().orElse(null))
        .procedureIdentifier(procedureIdentifier())
        .airportIdentifier(airportIdentifier())
        .airportRegion(airportRegion())
        .procedureType(procedureType())
        .transitionType(transitionType())
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
    BoogieTransition that = (BoogieTransition) o;
    return Objects.equals(transitionIdentifier, that.transitionIdentifier) &&
        Objects.equals(procedureIdentifier, that.procedureIdentifier) &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportRegion, that.airportRegion) &&
        procedureType == that.procedureType &&
        transitionType == that.transitionType &&
        Objects.equals(legs, that.legs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transitionIdentifier, procedureIdentifier, airportIdentifier, airportRegion, procedureType, transitionType, legs);
  }

  @Override
  public String toString() {
    return "BoogieTransition{" +
        "transitionIdentifier='" + transitionIdentifier + '\'' +
        ", procedureIdentifier='" + procedureIdentifier + '\'' +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportRegion='" + airportRegion + '\'' +
        ", procedureType=" + procedureType +
        ", transitionType=" + transitionType +
        ", legs=" + legs +
        '}';
  }

  public static final class Builder {
    private String transitionIdentifier;
    private String procedureIdentifier;
    private String airportIdentifier;
    private String airportRegion;
    private ProcedureType procedureType;
    private TransitionType transitionType;
    private List<BoogieLeg> legs;

    public Builder transitionIdentifier(String transitionIdentifier) {
      this.transitionIdentifier = transitionIdentifier;
      return this;
    }

    public Builder procedureIdentifier(String procedureIdentifier) {
      this.procedureIdentifier = requireNonNull(procedureIdentifier);
      return this;
    }

    public Builder airportIdentifier(String airportIdentifier) {
      this.airportIdentifier = requireNonNull(airportIdentifier);
      return this;
    }

    public Builder airportRegion(String airportRegion) {
      this.airportRegion = requireNonNull(airportRegion);
      return this;
    }

    public Builder procedureType(ProcedureType procedureType) {
      this.procedureType = requireNonNull(procedureType);
      return this;
    }

    public Builder transitionType(TransitionType transitionType) {
      this.transitionType = requireNonNull(transitionType);
      return this;
    }

    public Builder legs(List<BoogieLeg> legs) {
      this.legs = legs;
      return this;
    }

    public BoogieTransition build() {
      return new BoogieTransition(this);
    }
  }
}
