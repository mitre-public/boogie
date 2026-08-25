package org.mitre.tdp.boogie.arinc.assemble;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;

import com.google.common.collect.Range;

/**
 * Strategy class for generating user-defined records from 424 restrictive airspace information. Used with {@link RestrictiveAirspaceAssembler}.
 */
public interface RestrictiveAirspaceAssemblyStrategy<A, AS> {

  /**
   * Assembly strategy for building {@link Airspace.Standard} and {@link AirspaceSequence.Standard} from restrictive airspace records.
   * @return the strategy
   */
  static RestrictiveAirspaceAssemblyStrategy<Airspace, AirspaceSequence> standard() {
    return new Standard();
  }

  A convertRestrictiveAirspace(ArincRestrictiveAirspaceLeg representative, List<AS> sequences);
  AS convertRestrictiveAirspaceSequence(ArincRestrictiveAirspaceLeg leg);

  final class Standard implements RestrictiveAirspaceAssemblyStrategy<Airspace, AirspaceSequence> {

    private Standard() {
    }

    @Override
    public Airspace convertRestrictiveAirspace(ArincRestrictiveAirspaceLeg representative, List<AirspaceSequence> sequences) {
      Range<Double> alts = AirwayAltitudeRange.INSTANCE.apply(representative.lowerLimit().orElse(null), null, representative.upperLimit().orElse(null));
      String ident = RestrictiveAirspaceName.INSTANCE.apply(representative);
      return Airspace.builder()
          .area(representative.customerAreaCode().name())
          .identifier(ident)
          .altitudeLimit(alts)
          .airspaceType(AirspaceType.RESTRICTIVE)
          .sequences(sequences)
          .build();
    }

    @Override
    public AirspaceSequence convertRestrictiveAirspaceSequence(ArincRestrictiveAirspaceLeg leg) {
      Geometry geometry = leg.boundaryVia()
          .flatMap(ViaToBoundary.INSTANCE::apply)
          .orElseThrow(() -> new IllegalArgumentException("No valid via coded for restrictive airspace leg"));
      LatLong centerFix = leg.arcOriginLatitude()
          .filter(lat -> leg.arcOriginLongitude().isPresent())
          .map(lat -> LatLong.of(lat, leg.arcOriginLongitude().orElseThrow()))
          .orElse(null);
      LatLong associatedFix = leg.latitude()
          .filter(lat -> leg.longitude().isPresent())
          .map(lat -> LatLong.of(lat, leg.longitude().orElseThrow()))
          .orElse(null);
      return AirspaceSequence.builder(geometry, leg.sequenceNumber())
          .centerFix(centerFix)
          .associatedFix(associatedFix)
          .build();
    }
  }
}
