package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;

public interface FirUirAssembler<AIRSPACE> {
  static FirUirAssembler<Airspace> standard() {
    return usingStrategy(FirUirAssemblyStrategy.standard());
  }

  Stream<AIRSPACE> assemble(Collection<ArincFirUirLeg> legs);

  static <AIRSPACE, SEQUENCE> FirUirAssembler<AIRSPACE> usingStrategy(FirUirAssemblyStrategy<AIRSPACE, SEQUENCE> assemblyStrategy) {
    return new Standard<>(assemblyStrategy);
  }

  final class Standard<AIRSPACE, SEQUENCE> implements FirUirAssembler<AIRSPACE> {
    private final FirUirAssemblyStrategy<AIRSPACE, SEQUENCE> assemblyStrategy;
    private Standard(FirUirAssemblyStrategy<AIRSPACE, SEQUENCE> assemblyStrategy) {
      this.assemblyStrategy = assemblyStrategy;
    }

    @Override
    public Stream<AIRSPACE> assemble(Collection<ArincFirUirLeg> legs) {
      Map<String, List<ArincFirUirLeg>> groupedUp = legs.stream()
          .sorted(Comparator.comparing(ArincFirUirLeg::sequenceNumber))
          .collect(Collectors.groupingBy(this::firUirKey));
      return groupedUp
          .values().stream()
          .flatMap(this::toAirspace);
    }

    private Stream<AIRSPACE> toAirspace(List<ArincFirUirLeg> legs) {
      ArincFirUirLeg representative = legs.get(0);
      List<SEQUENCE> sequences = legs.stream().map(assemblyStrategy::convertFirUirLeg).collect(Collectors.toList());
      return assemblyStrategy.convertFirUir(representative, sequences);
    }

    private String firUirKey(ArincFirUirLeg leg) {
      return leg.customerAreaCode().name()
          .concat(leg.firUirIdentifier())
          .concat(leg.firUirAddress().orElse(""))
          .concat(leg.firUirIndicator().name());
    }
  }
}
