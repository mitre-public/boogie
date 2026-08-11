package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.util.Partitioners;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;

/**
 * This class converts ARINC restrictive airspace legs into client records.
 * @param <AIRSPACE> the class of Airspace you want to create.
 */
public interface RestrictiveAirspaceAssembler<AIRSPACE> {

  Stream<AIRSPACE> assemble(Collection<ArincRestrictiveAirspaceLeg> legs);

  /**
   * Creates an assembler that produces standard boogie {@link Airspace} records.
   * @return the assembler for downstream use.
   */
  static RestrictiveAirspaceAssembler<Airspace> standard() {
    return usingStrategy(RestrictiveAirspaceAssemblyStrategy.standard());
  }

  /**
   * Creates an assembler using the provided strategy.
   * @param airspaceStrategy the strategy to create client airspace.
   * @return the client airspace assembler.
   * @param <AIRSPACE> the class of airspace
   * @param <SEQUENCE> the class of airspace sequence
   */
  static <AIRSPACE, SEQUENCE> RestrictiveAirspaceAssembler<AIRSPACE> usingStrategy(RestrictiveAirspaceAssemblyStrategy<AIRSPACE, SEQUENCE> airspaceStrategy) {
    return new Standard<>(airspaceStrategy);
  }

  final class Standard<AIRSPACE, SEQUENCE> implements RestrictiveAirspaceAssembler<AIRSPACE> {

    private final RestrictiveAirspaceAssemblyStrategy<AIRSPACE, SEQUENCE> airspaceAssemblyStrategy;

    private Standard(RestrictiveAirspaceAssemblyStrategy<AIRSPACE, SEQUENCE> airspaceAssemblyStrategy) {
      this.airspaceAssemblyStrategy = airspaceAssemblyStrategy;
    }

    @Override
    public Stream<AIRSPACE> assemble(Collection<ArincRestrictiveAirspaceLeg> legs) {
      return legs.stream()
          .collect(Collectors.groupingBy(this::restrictiveKey))
          .values()
          .stream()
          .map(this::toAirspace);
    }

    private AIRSPACE toAirspace(List<ArincRestrictiveAirspaceLeg> legs) {
      List<SEQUENCE> sequences = legs.stream()
          .sorted(Comparator.comparingInt(l -> l.sequenceNumber().orElse(0)))
          .map(airspaceAssemblyStrategy::convertRestrictiveAirspaceSequence)
          .toList();
      ArincRestrictiveAirspaceLeg firstLeg = legs.get(0);
      return airspaceAssemblyStrategy.convertRestrictiveAirspace(firstLeg, sequences);
    }

    private String restrictiveKey(ArincRestrictiveAirspaceLeg leg) {
      return leg.icaoRegion()
          .concat(leg.restrictiveAirspaceDesignation())
          .concat(leg.restrictiveType())
          .concat(leg.multipleCode().orElse(""));
    }
  }
}
