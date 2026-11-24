package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


/**
 * * Strategy class for generating used-defined records from 424 information. Used with {@link
 * ControlledAirspaceAssembler}.
 */
public interface ControlledAirspaceAssemblyStrategy<A, F, AS> {

  /**
   * Assembly strategy for building {@link Airspace.Standard } and {@link AirspaceSequence.Standard} from records defined in ARINC 424.
   *
   * @return the strategy
   */
  static ControlledAirspaceAssemblyStrategy<Airspace, Fix, AirspaceSequence> standard() {
    return new Standard();
  }

  A convertControlledAirspace(ArincControlledAirspaceLeg representative, @Nullable F fix, List<AS> sequences);

  AS convertControlledAirspaceSequence(ArincControlledAirspaceLeg leg);

  final class Standard implements ControlledAirspaceAssemblyStrategy<Airspace, Fix, AirspaceSequence> {

    private Standard() {
    }

    @Override
    public Airspace convertControlledAirspace(ArincControlledAirspaceLeg representative, @Nullable Fix fix, List<AirspaceSequence> sequences) {
      String identifier = ControlledAirspaceName.INSTANCE.apply(representative);
      return Airspace.builder()
          .area(representative.customerAreaCode().name())
          .identifier(identifier)
          .altitudeLimits(representative.altitudeLimits())
          .airspaceType(AirspaceType.CONTROLLED)
          .sequences(sequences)
          .centerIdentification(CenterIdentification.builder(representative.airspaceCenter())
              .area(representative.customerAreaCode().name())
              .icaoRegion(representative.icaoRegion())
              .type(centerType(representative))
              .build())
          .center(fix)
          .build();
    }

    private BoogieType centerType(ArincControlledAirspaceLeg representative) {
      return representative.supplierSectionCode()
          .map(section -> switch (section.name() + representative.supplierSubSectionCode().orElse("")) {
            case "PA" -> BoogieType.AIRPORT;
            case "HA" -> BoogieType.HELIPORT;
            case "D", "DB", "PN", "EA" -> BoogieType.FIX;
            case "UF" -> BoogieType.AIRSPACE;
            default -> null;
          })
          .orElse(null);
    }

    @Override
    public AirspaceSequence convertControlledAirspaceSequence(ArincControlledAirspaceLeg leg) {
      Geometry geometry = Optional.of(leg.boundaryVia())
          .flatMap(ViaToBoundary.INSTANCE)
          .orElseThrow(() -> new IllegalArgumentException("No valid via coded"));
      return AirspaceSequenceAssembly.INSTANCE.apply(leg, geometry);
    }
  }
}
