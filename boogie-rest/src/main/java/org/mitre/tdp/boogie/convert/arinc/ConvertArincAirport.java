package org.mitre.tdp.boogie.convert.arinc;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.arinc.ArincAirport;
import org.mitre.tdp.boogie.contract.arinc.ImmutableArincAirport;

public final class ConvertArincAirport implements Function<org.mitre.tdp.boogie.arinc.model.ArincAirport, ArincAirport> {

  public static final ConvertArincAirport INSTANCE = new ConvertArincAirport();

  private ConvertArincAirport() {

  }

  @Override
  public ArincAirport apply(org.mitre.tdp.boogie.arinc.model.ArincAirport arincAirport) {
    requireNonNull(arincAirport);
    return ImmutableArincAirport.builder()
        .recordType(arincAirport.recordType())
        .customerAreaCode(arincAirport.customerAreaCode())
        .sectionCode(arincAirport.sectionCode())
        .airportIdentifier(arincAirport.airportIdentifier())
        .airportIcaoRegion(arincAirport.airportIcaoRegion())
        .subSectionCode(arincAirport.subSectionCode())
        .iataDesignator(arincAirport.iataDesignator())
        .continuationRecordNumber(arincAirport.continuationRecordNumber())
        .speedLimitAltitude(arincAirport.speedLimitAltitude())
        .longestRunway(arincAirport.longestRunway())
        .ifrCapability(arincAirport.ifrCapability())
        .longestRunwaySurfaceCode(arincAirport.longestRunwaySurfaceCode())
        .latitude(arincAirport.latitude())
        .longitude(arincAirport.longitude())
        .magneticVariation(arincAirport.magneticVariation())
        .airportElevation(arincAirport.airportElevation())
        .speedLimit(arincAirport.speedLimit())
        .recommendedNavaid(arincAirport.recommendedNavaid())
        .recommendedNavaidIcaoRegion(arincAirport.recommendedNavaidIcaoRegion())
        .transitionAltitude(arincAirport.transitionAltitude())
        .transitionLevel(arincAirport.transitionLevel())
        .publicMilitaryIndicator(arincAirport.publicMilitaryIndicator())
        .daylightTimeIndicator(arincAirport.daylightTimeIndicator())
        .magneticTrueIndicator(arincAirport.magneticTrueIndicator())
        .datumCode(arincAirport.datumCode())
        .airportFullName(arincAirport.airportFullName())
        .fileRecordNumber(arincAirport.fileRecordNumber())
        .lastUpdateCycle(arincAirport.lastUpdateCycle())
        .build();
  }
}
