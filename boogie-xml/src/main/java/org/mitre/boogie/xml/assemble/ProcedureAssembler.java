package org.mitre.boogie.xml.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mitre.boogie.xml.database.FixDatabase;
import org.mitre.boogie.xml.database.FixDatabaseFactory;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincTransition;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;

/**
 * Assembler class for converting {@link ArincProcedure} records into a client-defined output class representing a procedure.
 *
 * <p>Each XML procedure contains transitions which contain legs. Legs reference fixes by IDREF elements. The assembler
 * uses a {@link FixDatabase} to resolve those references into assembled fix objects.
 *
 * <p>Procedures are nested within {@link ArincPortInfo} (on airports and heliports), so the assembler iterates over all
 * airports and heliports in the provided {@link ArincRecords} to collect all procedures.
 *
 * <p>This class can be used with {@link ProcedureAssemblyStrategy#standard()} and
 * {@link FixDatabaseFactory#standard(ArincRecords)} to generate lightweight Boogie-defined {@link Procedure} implementations.
 */
public interface ProcedureAssembler<P> {

  static ProcedureAssembler<Procedure> standard(FixDatabase<Fix> fixDatabase) {
    return withStrategy(ProcedureAssemblyStrategy.standard(), fixDatabase);
  }

  static <P, T, L, F> ProcedureAssembler<P> withStrategy(ProcedureAssemblyStrategy<P, T, L, F> strategy, FixDatabase<F> fixDatabase) {
    return new Standard<>(strategy, fixDatabase);
  }

  Collection<P> assemble(ArincRecords records);

  final class Standard<P, T, L, F> implements ProcedureAssembler<P> {

    private final ProcedureAssemblyStrategy<P, T, L, F> strategy;
    private final FixDatabase<F> fixDatabase;

    private Standard(ProcedureAssemblyStrategy<P, T, L, F> strategy, FixDatabase<F> fixDatabase) {
      this.strategy = requireNonNull(strategy);
      this.fixDatabase = requireNonNull(fixDatabase);
    }

    @Override
    public Collection<P> assemble(ArincRecords records) {
      return records.airports().stream()
          .flatMap(airport -> assembleForPort(airport.portInfo()).stream())
          .collect(Collectors.toList());
    }

    private List<P> assembleForPort(ArincPortInfo portInfo) {
      String airportIdentifier = portInfo.pointInfo().identifier();

      return portInfo.procedures().orElse(List.of()).stream()
          .map(proc -> assembleOne(proc, airportIdentifier))
          .collect(Collectors.toList());
    }

    private P assembleOne(ArincProcedure procedure, String airportIdentifier) {
      List<T> transitions = procedure.transitions().stream()
          .map(transition -> convertTransition(procedure, transition))
          .collect(Collectors.toList());

      return strategy.convertProcedure(procedure, airportIdentifier, transitions);
    }

    private T convertTransition(ArincProcedure procedure, ArincTransition transition) {
      List<L> legs = transition.legs().stream()
          .map(this::convertLeg)
          .collect(Collectors.toList());

      return strategy.convertTransition(procedure, transition, legs);
    }

    private L convertLeg(ArincProcedureLeg leg) {
      F associatedFix = leg.fixRef().flatMap(fixDatabase::fix).orElse(null);
      F recommendedNavaid = leg.recNavaidRef().flatMap(fixDatabase::fix).orElse(null);
      F centerFix = leg.centerFixRef().flatMap(fixDatabase::fix).orElse(null);
      return strategy.convertLeg(leg, associatedFix, recommendedNavaid, centerFix);
    }
  }
}
