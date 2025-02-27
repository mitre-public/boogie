package org.mitre.tdp.boogie.arinc.assemble;

import java.util.List;

import javax.annotation.Nullable;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;

import com.google.common.collect.Range;

/**
 * Strategy class for generating used-defined records from 424 information. Used with {@link ControlledAirspaceAssembler}.
 */
public interface ControlledAirspaceAssemblyStrategy<A, F, AS> {

  /**
   * Assembly strategy for building {@link Airspace.Standard } and {@link AirspaceSequence.Standard} from records defined in ARINC 424.
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

      Range<Double> alts = AirwayAltitudeRange.INSTANCE.apply(representative.lowerLimit().orElse(null), null, representative.upperLimit().orElse(null));

      return Airspace.builder()
          .area(representative.customerAreaCode().name())
          .identifier(representative.airspaceCenter().concat("-").concat(representative.airspaceType().name()).concat("-").concat(representative.controlledAirspaceName().orElse("")))
          .altitudeLimit(alts)
          .airspaceType(AirspaceType.CONTROLLED)
          .sequences(sequences)
          .centerIdent(representative.airspaceCenter())
          .center(fix)
          .build();
    }

    @Override
    public AirspaceSequence convertControlledAirspaceSequence(ArincControlledAirspaceLeg leg) {
      Geometry geometry = ViaToBoundary.INSTANCE.apply(leg.boundaryVia()).orElseThrow(() -> new IllegalArgumentException("No valid via coded"));
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
