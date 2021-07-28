package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * Functional class for converting a collection of {@link ArincProcedureLeg} into a sequence of {@link Transition} records as
 * would be expected in downstream data classes in packages like boogie-routes.
 */
public final class TransitionAssembler implements Function<Collection<ArincProcedureLeg>, Stream<Transition>> {

  private final TerminalAreaDatabase terminalAreaDatabase;
  private final FixDatabase fixDatabase;

  public TransitionAssembler(TerminalAreaDatabase terminalAreaDatabase, FixDatabase fixDatabase) {
    this.terminalAreaDatabase = requireNonNull(terminalAreaDatabase);
    this.fixDatabase = requireNonNull(fixDatabase);
  }

  @Override
  public Stream<Transition> apply(Collection<ArincProcedureLeg> arincProcedureLegs) {
    return Stream.empty();
  }
}
