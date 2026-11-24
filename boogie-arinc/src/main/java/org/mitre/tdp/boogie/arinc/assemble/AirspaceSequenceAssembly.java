package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.BiFunction;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.model.ArincAirspaceLeg;

final class AirspaceSequenceAssembly implements BiFunction<ArincAirspaceLeg, Geometry, AirspaceSequence> {
  static final AirspaceSequenceAssembly INSTANCE = new AirspaceSequenceAssembly();

  private AirspaceSequenceAssembly() {}

  @Override
  public AirspaceSequence apply(ArincAirspaceLeg leg, Geometry geometry) {
    LatLong centerFix = leg.arcOriginLatitude()
        .flatMap(lat -> leg.arcOriginLongitude().map(lon -> LatLong.of(lat, lon)))
        .orElse(null);
    LatLong associatedFix = leg.latitude()
        .flatMap(lat -> leg.longitude().map(lon -> LatLong.of(lat, lon)))
        .orElse(null);
    return AirspaceSequence.builder(geometry, leg.sequenceNumber())
        .centerFix(centerFix)
        .associatedFix(associatedFix)
        .arcRadius(leg.arcDistance().orElse(null))
        .arcBearing(leg.arcBearing().orElse(null))
        .build();
  }
}
