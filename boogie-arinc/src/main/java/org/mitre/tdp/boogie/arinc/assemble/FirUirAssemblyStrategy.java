package org.mitre.tdp.boogie.arinc.assemble;

import java.util.List;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;


/**
 * * Strategy class for generating used-defined records from 424 information. Used with {@link FirUirAssembler}.
 */
public interface FirUirAssemblyStrategy<A, S> {

  /**
   * Assembly strategy for building {@link Airspace.Standard } and {@link AirspaceSequence.Standard} from records defined in ARINC 424.
   * @return the strategy
   */
  static FirUirAssemblyStrategy<Airspace, AirspaceSequence> standard() {
    return new Standard();
  }

  /**
   * This generates the airspace from arinc 424
   * @param representative the first leg that has special data on it
   * @param allLegs all the converted egs
   * @return the record made from ARINC 424
   */
  Stream<A> convertFirUir(ArincFirUirLeg representative, List<S> allLegs);

  /**
   * This generates the sequences from ARINC 424
   * @param leg the leg to convert
   * @return the sequence made from that leg
   */
  S convertFirUirLeg(ArincFirUirLeg leg);

  /**
   * * This is the standard strategy that produces the simplified objects.
   */
  final class Standard implements FirUirAssemblyStrategy<Airspace, AirspaceSequence> {
    private Standard() {}

    @Override
    public Stream<Airspace> convertFirUir(ArincFirUirLeg representative, List<AirspaceSequence> allLegs) {
      return switch (representative.firUirIndicator()) {
        case F -> Stream.of(buildAirspace(representative, allLegs, AirspaceType.FIR));
        case U -> Stream.of(buildAirspace(representative, allLegs, AirspaceType.UIR));
        case B -> Stream.of(
            buildAirspace(representative, allLegs, AirspaceType.FIR),
            buildAirspace(representative, allLegs, AirspaceType.UIR));
        case SPEC -> Stream.empty();
      };
    }

    private Airspace buildAirspace(ArincFirUirLeg leg, List<AirspaceSequence> sequences, AirspaceType type) {
      String identifier = leg.firUirIdentifier();
      return Airspace.builder()
          .area(leg.customerAreaCode().name())
          .identifier(leg.firUirAddress().map(address -> identifier.concat("-").concat(address)).orElse(identifier))
          .altitudeLimits(type == AirspaceType.FIR ? leg.firAltitudeLimits() : leg.uirAltitudeLimits())
          .airspaceType(type)
          .sequences(sequences)
          .build();
    }

    @Override
    public AirspaceSequence convertFirUirLeg(ArincFirUirLeg leg) {
      Geometry geometry = ViaToBoundary.INSTANCE.apply(leg.boundaryVia()).orElseThrow(() -> new IllegalArgumentException("No valid via code"));
      return AirspaceSequenceAssembly.INSTANCE.apply(leg, geometry);
    }
  }
}
