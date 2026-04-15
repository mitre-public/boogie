package org.mitre.boogie.xml.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.boogie.xml.database.XmlFixDatabase;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincAirwayLeg;

/**
 * Assembler class for converting a single {@link ArincAirway} record into a client-defined output class representing an airway.
 *
 * <p>Each XML airway already contains its legs inline, but the legs reference fixes by identifier. The assembler uses a
 * {@link XmlFixDatabase} to resolve those references into assembled fix objects.
 *
 * <p>This class can be used with {@link AirwayAssemblyStrategy#standard()} and a {@link XmlFixDatabase} to generate
 * lightweight Boogie-defined {@link Airway} implementations.
 */
public interface AirwayAssembler<A> {

  static AirwayAssembler<Airway> standard(XmlFixDatabase<Fix> xmlFixDatabase) {
    return withStrategy(AirwayAssemblyStrategy.standard(), xmlFixDatabase);
  }

  static <A, F, L> AirwayAssembler<A> withStrategy(AirwayAssemblyStrategy<A, F, L> strategy, XmlFixDatabase<F> xmlFixDatabase) {
    return new Standard<>(strategy, xmlFixDatabase);
  }

  Stream<A> assemble(Collection<ArincAirway> airways);

  final class Standard<A, F, L> implements AirwayAssembler<A> {

    private final AirwayAssemblyStrategy<A, F, L> strategy;
    private final XmlFixDatabase<F> xmlFixDatabase;

    private Standard(AirwayAssemblyStrategy<A, F, L> strategy, XmlFixDatabase<F> xmlFixDatabase) {
      this.strategy = requireNonNull(strategy);
      this.xmlFixDatabase = requireNonNull(xmlFixDatabase);
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
      F associatedFix = leg.fixRef().flatMap(xmlFixDatabase::fix).orElse(null);
      F recommendedNavaid = leg.recNavaidRef().flatMap(xmlFixDatabase::fix).orElse(null);
      return strategy.convertLeg(leg, associatedFix, recommendedNavaid);
    }
  }
}
