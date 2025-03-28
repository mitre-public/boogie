package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.util.Partitioners;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * This class converts arinc controlled airspace into client records
 * @param <AIRSPACE> the class of Airspace you want to create.
 */
public interface ControlledAirspaceAssembler<AIRSPACE> {
  Stream<AIRSPACE> assemble(Collection<ArincControlledAirspaceLeg> legs);

  /**
   * This creates an assembler that creates standard boogie records.
   * @param fixes all the fixes.
   * @return the assembler for down stream use.
   */
  static ControlledAirspaceAssembler<Airspace> standard(ArincFixDatabase fixes) {
    return usingStrategy(fixes, FixAssemblyStrategy.standard(), ControlledAirspaceAssemblyStrategy.standard());
  }

  /**
   * This class creates an airspace per the definition of the client records.
   * @param fixes all the fixes.
   * @param fixStrategy the strategy to create client fixes
   * @param airspaceStrategy the strategy to create client airspace.
   * @return the client airspace assembler.
   * @param <AIRSPACE> the class of airspace
   * @param <FIX> the class of fix
   * @param <SEQUENCE> the class of airspace sequence, its certainly not a leg
   */
  static <AIRSPACE, FIX, SEQUENCE> ControlledAirspaceAssembler<AIRSPACE> usingStrategy(ArincFixDatabase fixes, FixAssemblyStrategy<FIX> fixStrategy, ControlledAirspaceAssemblyStrategy<AIRSPACE, FIX, SEQUENCE> airspaceStrategy) {
    return new Standard<>(fixes, fixStrategy, airspaceStrategy);
  }

  final class Standard<AIRSPACE, FIX, SEQUENCE> implements ControlledAirspaceAssembler<AIRSPACE> {
    private final FixDereferencer<FIX> inflator;
    private final ControlledAirspaceAssemblyStrategy<AIRSPACE, FIX, SEQUENCE> airspaceAssemblyStrategy;

    private Standard(ArincFixDatabase fixes, FixAssemblyStrategy<FIX> fixStrategy, ControlledAirspaceAssemblyStrategy<AIRSPACE, FIX, SEQUENCE> airspaceAssemblyStrategy) {
      this.inflator = new FixDereferencer<>(FixAssembler.withStrategy(fixStrategy), ArincDatabaseFactory.emptyTerminalAreaDatabase(), fixes);
      this.airspaceAssemblyStrategy = airspaceAssemblyStrategy;
    }

    @Override
    public Stream<AIRSPACE> assemble(Collection<ArincControlledAirspaceLeg> legs) {
      return legs.stream()
          .collect(Collectors.groupingBy(this::controlledKey))
          .values()
          .stream()
          .map(this::toAirspace);
    }

    private AIRSPACE toAirspace(List<ArincControlledAirspaceLeg> legs) {
      List<SEQUENCE> sequences = legs.stream()
          .sorted(Comparator.comparing(ArincControlledAirspaceLeg::sequenceNumber))
          .map(airspaceAssemblyStrategy::convertControlledAirspaceSequence)
          .toList();
      ArincControlledAirspaceLeg firstLeg = legs.get(0);
      FIX center = firstLeg.supplierSectionCode()
          .filter(s -> !SectionCode.U.equals(s))
          .filter(s -> !SectionCode.H.equals(s)) //this is just for now
          .flatMap(f -> inflator.dereference(firstLeg.airspaceCenter(), "", firstLeg.icaoRegion(), firstLeg.supplierSectionCode().orElseThrow(), firstLeg.supplierSubSectionCode().orElse(null)))
          .orElse(null);
      return airspaceAssemblyStrategy.convertControlledAirspace(firstLeg, center, sequences);
    }

    private String controlledKey(ArincControlledAirspaceLeg leg) {
      return leg.airspaceCenter().concat(leg.airspaceType().name()).concat(leg.controlledAirspaceName().orElse(""));
    }
  }
}
