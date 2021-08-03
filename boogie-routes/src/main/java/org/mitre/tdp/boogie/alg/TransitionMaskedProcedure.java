package org.mitre.tdp.boogie.alg;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;

/**
 * Returns a new {@link Procedure} record decorating the provided procedure but with the {@link Procedure#transitions()} masked
 * according to the provided transition predicate.
 * <br>
 * This class is generally useful for wrapping input procedures such that only portions of them that we want are exposed without
 * having the modify the underling procedure definition.
 */
public final class TransitionMaskedProcedure implements Procedure {

  private final Procedure procedure;
  private final List<Transition> filteredTransitions;

  public TransitionMaskedProcedure(Procedure procedure, Predicate<Transition> transitionFilter) {
    this.procedure = requireNonNull(procedure);
    requireNonNull(transitionFilter, "Transition filter must be provided");
    this.filteredTransitions = procedure.transitions().stream().filter(transitionFilter).collect(Collectors.toList());
  }

  @Override
  public String procedureIdentifier() {
    return procedure.procedureIdentifier();
  }

  @Override
  public String airportIdentifier() {
    return procedure.airportIdentifier();
  }

  @Override
  public String airportRegion() {
    return procedure.airportRegion();
  }

  @Override
  public ProcedureType procedureType() {
    return procedure.procedureType();
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return procedure.requiredNavigationEquipage();
  }

  @Override
  public Collection<? extends Transition> transitions() {
    return filteredTransitions;
  }
}
