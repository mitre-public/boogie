package org.mitre.tdp.boogie.convert.arinc;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.arinc.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.contract.arinc.ImmutableArincLocalizerGlideSlope;

public final class ConvertArincLocalizerGlideSlope implements Function<org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope, ArincLocalizerGlideSlope> {

  public static final ConvertArincLocalizerGlideSlope INSTANCE = new ConvertArincLocalizerGlideSlope();

  private ConvertArincLocalizerGlideSlope() {

  }

  @Override
  public ArincLocalizerGlideSlope apply(org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope arincLocalizerGlideSlope) {
    return ImmutableArincLocalizerGlideSlope.builder()
        .recordType(arincLocalizerGlideSlope.recordType())
        .customerAreaCode(arincLocalizerGlideSlope.customerAreaCode())
        .sectionCode(arincLocalizerGlideSlope.sectionCode())
        .airportIdentifier(arincLocalizerGlideSlope.airportIdentifier())
        .airportIcaoRegion(arincLocalizerGlideSlope.airportIcaoRegion())
        .subSectionCode(arincLocalizerGlideSlope.subSectionCode())
        .localizerIdentifier(arincLocalizerGlideSlope.localizerIdentifier())
        .ilsMlsGlsCategory(arincLocalizerGlideSlope.ilsMlsGlsCategory())
        .continuationRecordNumber(arincLocalizerGlideSlope.continuationRecordNumber())
        .localizerFrequency(arincLocalizerGlideSlope.localizerFrequency())
        .runwayIdentifier(arincLocalizerGlideSlope.runwayIdentifier())
        .localizerLatitude(arincLocalizerGlideSlope.localizerLatitude())
        .localizerLongitude(arincLocalizerGlideSlope.localizerLongitude())
        .localizerBearing(arincLocalizerGlideSlope.localizerBearing())
        .glideSlopeLatitude(arincLocalizerGlideSlope.glideSlopeLatitude())
        .glideSlopeLongitude(arincLocalizerGlideSlope.glideSlopeLongitude())
        .localizerPosition(arincLocalizerGlideSlope.localizerPosition())
        .localizerPositionReference(arincLocalizerGlideSlope.localizerPositionReference())
        .glideSlopePosition(arincLocalizerGlideSlope.glideSlopePosition())
        .localizerWidth(arincLocalizerGlideSlope.localizerWidth())
        .glideSlopeAngle(arincLocalizerGlideSlope.glideSlopeAngle())
        .stationDeclination(arincLocalizerGlideSlope.stationDeclination())
        .glideSlopeHeightAtLandingThreshold(arincLocalizerGlideSlope.glideSlopeHeightAtLandingThreshold())
        .glideSlopeElevation(arincLocalizerGlideSlope.glideSlopeElevation())
        .supportingFacilityIdentifier(arincLocalizerGlideSlope.supportingFacilityIdentifier())
        .supportingFacilityIcaoCode(arincLocalizerGlideSlope.supportingFacilityIcaoCode())
        .supportingFacilitySectionCode(arincLocalizerGlideSlope.supportingFacilitySectionCode())
        .supportingFacilitySubSectionCode(arincLocalizerGlideSlope.supportingFacilitySubSectionCode())
        .fileRecordNumber(arincLocalizerGlideSlope.fileRecordNumber())
        .lastUpdateCycle(arincLocalizerGlideSlope.lastUpdateCycle())
        .build();
  }
}
