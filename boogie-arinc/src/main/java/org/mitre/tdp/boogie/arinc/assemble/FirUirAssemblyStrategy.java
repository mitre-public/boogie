package org.mitre.tdp.boogie.arinc.assemble;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirIndicator;

import com.google.common.collect.Range;

/**
 * Strategy class for generating used-defined records from 424 information. Used with {@link FirUirAssembler}.
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
   * This is the standard strategy that produces the simplified objects.
   */
  final class Standard implements FirUirAssemblyStrategy<Airspace, AirspaceSequence> {
    private Standard() {}

    @Override
    public Stream<Airspace> convertFirUir(ArincFirUirLeg representative, List<AirspaceSequence> allLegs) {
      Airspace fir = Optional.of(representative)
          .filter(i -> i.firUirIndicator().equals(FirUirIndicator.F) || i.firUirIndicator().equals(FirUirIndicator.B))
          .map(i -> {
            Range<Double> firLimit = i.firUpperLimit().map(Range::atMost).orElseGet(Range::all);
            return Airspace.builder()
                .area(i.customerAreaCode().name())
                .identifier(i.firUirAddress().map(a -> i.firUirIdentifier().concat("-").concat(a)).orElse(i.firUirIdentifier()))
                .altitudeLimit(firLimit)
                .airspaceType(AirspaceType.FIR)
                .sequences(allLegs)
                .build();
          })
          .orElse(null);

      Airspace uir = Optional.of(representative)
          .filter(i -> i.firUirIndicator().equals(FirUirIndicator.U) || i.firUirIndicator().equals(FirUirIndicator.B))
          .map(i -> {
            Range<Double> uirLimit = LimitsToRange.INSTANCE.apply(i.uirLowerLimit().orElse(null), i.uirUpperLimit().orElse(null));
            return Airspace.builder()
                .area(i.customerAreaCode().name())
                .identifier(i.firUirAddress().map(a -> i.firUirIdentifier().concat("-").concat(a)).orElse(i.firUirIdentifier()))
                .altitudeLimit(uirLimit)
                .airspaceType(AirspaceType.UIR)
                .sequences(allLegs)
                .build();
          })
          .orElse(null);

      return Stream.of(fir, uir).filter(Objects::nonNull);
    }

    @Override
    public AirspaceSequence convertFirUirLeg(ArincFirUirLeg leg) {
      Geometry geometry = ViaToBoundary.INSTANCE.apply(leg.boundaryVia()).orElseThrow(() -> new IllegalArgumentException("No valid via code"));
      LatLong centerFix = leg.arcOriginLatitude()
          .filter(lat -> leg.arcOriginLongitude().isPresent())
          .map(lat -> LatLong.of(lat, leg.arcOriginLongitude().orElseThrow()))
          .orElse(null);
      LatLong associatedFix = leg.firUirLatitude()
          .filter(lat -> leg.firUirLongitude().isPresent())
          .map(lat -> LatLong.of(lat, leg.firUirLongitude().orElseThrow()))
          .orElse(null);
      return AirspaceSequence.builder(geometry, leg.sequenceNumber())
          .centerFix(centerFix)
          .associatedFix(associatedFix)
          .build();
    }
  }
}
