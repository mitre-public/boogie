package org.mitre.boogie.xml.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.boogie.xml.database.FixDatabase;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincAirwayLeg;

/**
 * Assembler class for converting a single {@link ArincAirway} record into a client-defined output class representing an airway.
 *
 * <p>Each XML airway already contains its legs inline, but the legs reference fixes by identifier. The assembler uses a
 * {@link FixDatabase} to resolve those references into assembled fix objects.
 *
 * <p>This class can be used with {@link AirwayAssemblyStrategy#standard()} and a {@link FixDatabase} to generate
 * lightweight Boogie-defined {@link Airway} implementations.
 */
public interface AirwayAssembler<A> {

  static AirwayAssembler<Airway> standard(FixDatabase<Fix> fixDatabase) {
    return withStrategy(AirwayAssemblyStrategy.standard(), fixDatabase);
  }

  static <A, F, L> AirwayAssembler<A> withStrategy(AirwayAssemblyStrategy<A, F, L> strategy, FixDatabase<F> fixDatabase) {
    return new Standard<>(strategy, fixDatabase);
  }

  Stream<A> assemble(Collection<ArincAirway> airways);

  final class Standard<A, F, L> implements AirwayAssembler<A> {

    private final AirwayAssemblyStrategy<A, F, L> strategy;
    private final FixDatabase<F> fixDatabase;

    private Standard(AirwayAssemblyStrategy<A, F, L> strategy, FixDatabase<F> fixDatabase) {
      this.strategy = requireNonNull(strategy);
      this.fixDatabase = requireNonNull(fixDatabase);
    }

    @Override
    public Stream<A> assemble(Collection<ArincAirway> airways) {
      return airways.stream().map(this::assembleOne);
    }

    private A assembleOne(ArincAirway airway) {
      List<L> legs = airway.legs().stream()
          .map(this::convertLeg)
          .collect(Collectors.toList());

      return strategy.convertAirway(airway, legs);
    }

    private L convertLeg(ArincAirwayLeg leg) {
      F associatedFix = leg.fixRef().flatMap(fixDatabase::fix).orElse(null);
      F recommendedNavaid = leg.recNavaidRef().flatMap(fixDatabase::fix).orElse(null);
      return strategy.convertLeg(leg, associatedFix, recommendedNavaid);
    }
  }
}
