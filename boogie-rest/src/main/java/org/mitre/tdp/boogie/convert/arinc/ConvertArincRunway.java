package org.mitre.tdp.boogie.convert.arinc;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.arinc.ArincRunway;
import org.mitre.tdp.boogie.contract.arinc.ImmutableArincRunway;

public final class ConvertArincRunway implements Function<org.mitre.tdp.boogie.arinc.model.ArincRunway, ArincRunway> {
  public static final ConvertArincRunway INSTANCE = new ConvertArincRunway();

  private ConvertArincRunway() {

  }

  @Override
  public ArincRunway apply(org.mitre.tdp.boogie.arinc.model.ArincRunway arincRunway) {
    requireNonNull(arincRunway);
    return ImmutableArincRunway.builder()
        .recordType(arincRunway.recordType())
        .customerAreaCode(arincRunway.customerAreaCode())
        .sectionCode(arincRunway.sectionCode())
        .airportIdentifier(arincRunway.airportIdentifier())
        .airportIcaoRegion(arincRunway.airportIcaoRegion())
        .subSectionCode(arincRunway.subSectionCode())
        .runwayIdentifier(arincRunway.runwayIdentifier())
        .continuationRecordNumber(arincRunway.continuationRecordNumber())
        .runwayLength(arincRunway.runwayLength())
        .runwayMagneticBearing(arincRunway.runwayMagneticBearing())
        .latitude(arincRunway.latitude())
        .longitude(arincRunway.longitude())
        .runwayGradient(arincRunway.runwayGradient())
        .landingThresholdElevation(arincRunway.landingThresholdElevation())
        .thresholdDisplacementDistance(arincRunway.thresholdDisplacementDistance())
        .thresholdCrossingHeight(arincRunway.thresholdCrossingHeight())
        .runwayWidth(arincRunway.runwayWidth())
        .ilsMlsGlsIdentifier(arincRunway.ilsMlsGlsIdentifier())
        .ilsMlsGlsCategory(arincRunway.ilsMlsGlsCategory())
        .stopway(arincRunway.stopway())
        .secondaryIlsMlsGlsIdentifier(arincRunway.secondaryIlsMlsGlsIdentifier())
        .secondaryIlsMlsGlsCategory(arincRunway.secondaryIlsMlsGlsCategory())
        .runwayDescription(arincRunway.runwayDescription())
        .fileRecordNumber(arincRunway.fileRecordNumber())
        .lastUpdateCycle(arincRunway.lastUpdateCycle())
        .build();
  }
}
