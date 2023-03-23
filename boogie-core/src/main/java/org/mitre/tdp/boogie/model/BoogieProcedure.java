package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Objects;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;

/**
 * Immutable, buildable implementation of the {@link Procedure} interface provided for convenience.
 */
public final class BoogieProcedure implements Procedure {

  private final String procedureIdentifier;
  private final String airportIdentifier;
  private final String airportRegion;
  private final ProcedureType procedureType;
  private final RequiredNavigationEquipage requiredNavigationEquipage;
  private final Collection<BoogieTransition> transitions;

  private BoogieProcedure(Builder builder) {
    this.procedureIdentifier = builder.procedureIdentifier;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportRegion = builder.airportRegion;
    this.procedureType = builder.procedureType;
    this.requiredNavigationEquipage = builder.requiredNavigationEquipage;
    this.transitions = builder.transitions;
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
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return requiredNavigationEquipage;
  }

  @Override
  public Collection<BoogieTransition> transitions() {
    return transitions;
  }

  public Builder toBuilder() {
    return new Builder()
        .procedureIdentifier(procedureIdentifier())
        .airportIdentifier(airportIdentifier())
        .airportRegion(airportRegion())
        .procedureType(procedureType())
        .requiredNavigationEquipage(requiredNavigationEquipage())
        .transitions(transitions());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoogieProcedure that = (BoogieProcedure) o;
    return Objects.equals(procedureIdentifier, that.procedureIdentifier) &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportRegion, that.airportRegion) &&
        procedureType == that.procedureType &&
        requiredNavigationEquipage == that.requiredNavigationEquipage &&
        Objects.equals(transitions, that.transitions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(procedureIdentifier, airportIdentifier, airportRegion, procedureType, requiredNavigationEquipage, transitions);
  }

  @Override
  public String toString() {
    return "BoogieProcedure{" +
        "procedureIdentifier='" + procedureIdentifier + '\'' +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportRegion='" + airportRegion + '\'' +
        ", procedureType=" + procedureType +
        ", requiredNavigationEquipage=" + requiredNavigationEquipage +
        ", transitions=" + transitions +
        '}';
  }

  public static final class Builder {
    private String procedureIdentifier;
    private String airportIdentifier;
    private String airportRegion;
    private ProcedureType procedureType;
    private RequiredNavigationEquipage requiredNavigationEquipage;
    private Collection<BoogieTransition> transitions;

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

    public Builder requiredNavigationEquipage(RequiredNavigationEquipage requiredNavigationEquipage) {
      this.requiredNavigationEquipage = requireNonNull(requiredNavigationEquipage);
      return this;
    }

    public Builder transitions(Collection<BoogieTransition> transitions) {
      this.transitions = transitions;
      return this;
    }

    public BoogieProcedure build() {
      return new BoogieProcedure(this);
    }
  }
}
