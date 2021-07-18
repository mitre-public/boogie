package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

public final class TransitionStream implements Function<Collection<ArincProcedureLeg>, Stream<Transition>> {

  private final TerminalAreaDatabase terminalAreaDatabase;
  private final FixDatabase fixDatabase;

  public TransitionStream(TerminalAreaDatabase terminalAreaDatabase, FixDatabase fixDatabase) {
    this.terminalAreaDatabase = requireNonNull(terminalAreaDatabase);
    this.fixDatabase = requireNonNull(fixDatabase);
  }

  @Override
  public Stream<Transition> apply(Collection<ArincProcedureLeg> arincProcedureLegs) {
    return Stream.empty();
  }
}
