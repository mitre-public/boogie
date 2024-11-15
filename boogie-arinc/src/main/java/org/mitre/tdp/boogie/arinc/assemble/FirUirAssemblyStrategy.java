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

public interface FirUirAssemblyStrategy<A, S> {

  static FirUirAssemblyStrategy<Airspace, AirspaceSequence> standard() {
    return new Standard();
  }

  Stream<A> convertFirUir(ArincFirUirLeg representative, List<S> allLegs);
  S convertFirUirLeg(ArincFirUirLeg leg);

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
          .map(lat -> LatLong.of(lat, leg.arcOriginLongitude().get()))
          .orElse(null);
      return AirspaceSequence.builder(geometry, leg.sequenceNumber())
          .centerFix(centerFix)
          .associatedFix(LatLong.of(leg.firUirLatitude(), leg.firUirLongitude()))
          .build();
    }
  }
}
