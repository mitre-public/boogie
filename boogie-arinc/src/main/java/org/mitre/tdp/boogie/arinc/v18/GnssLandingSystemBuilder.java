package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;

public final class GnssLandingSystemBuilder implements Function<ArincRecord, ArincGnssLandingSystem.Builder> {
  public static final GnssLandingSystemBuilder INSTANCE = new GnssLandingSystemBuilder();
  private GnssLandingSystemBuilder() {}
  @Override
  public ArincGnssLandingSystem.Builder apply(ArincRecord arincRecord) {
    Optional<String> glsCategory = arincRecord.optionalField("glsCategory");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Double> glsApproachBearing = arincRecord.optionalField("glsApproachBearing");
    Optional<String> glsStationIdent = arincRecord.optionalField("glsStationIdentifier");
    Optional<Integer> serviceVolumes = arincRecord.optionalField("serviceVolumeRadius");
    Optional<String> tdmaSlots = arincRecord.optionalField("tdmaSlots");
    Optional<Double> glsApproachSlope = arincRecord.optionalField("glsApproachSlope");
    Optional<Double> stationElevation = arincRecord.optionalField("stationElevation");
    Optional<String> datumCode = arincRecord.optionalField("datumCode");
    Optional<String> stationType = arincRecord.optionalField("stationType");
    Optional<Double> stationElevationWgs84 = arincRecord.optionalField("stationElevationWgs84");

    return new ArincGnssLandingSystem.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .airportIdentifier(arincRecord.requiredField("airportIdentifier"))
        .airportIcaoRegion(arincRecord.requiredField("airportIcaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .glsRefPathIdentifier(arincRecord.requiredField("glsRefPathIdentifier"))
        .glsCategory(glsCategory.orElse(null))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .glsChannel(arincRecord.requiredField("glsChannel"))
        .runwayIdentifier(arincRecord.requiredField("runwayIdentifier"))
        .glsApproachBearing(glsApproachBearing.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .glsStationIdent(glsStationIdent.orElse(null))
        .serviceVolumeRadius(serviceVolumes.orElse(null))
        .tdmaSlots(tdmaSlots.orElse(null))
        .glsApproachSlope(glsApproachSlope.orElse(null))
        .magneticVariation(arincRecord.requiredField("magneticVariation"))
        .stationElevation(stationElevation.orElse(null))
        .datumCode(datumCode.orElse(null))
        .stationType(stationType.orElse(null))
        .stationElevationWgs84(stationElevationWgs84.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdatedCycle(arincRecord.requiredField("lastUpdatedCycle"));
  }
}
