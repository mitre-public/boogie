package org.mitre.tdp.boogie.convert.arinc;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.arinc.ArincVhfNavaid;
import org.mitre.tdp.boogie.contract.arinc.ImmutableArincVhfNavaid;

public final class ConvertArincVhfNavaid implements Function<org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid, ArincVhfNavaid> {
  public static final ConvertArincVhfNavaid INSTANCE = new ConvertArincVhfNavaid();

  private ConvertArincVhfNavaid() {

  }

  @Override
  public ArincVhfNavaid apply(org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid arincVhfNavaid) {
    requireNonNull(arincVhfNavaid);
    return ImmutableArincVhfNavaid.builder()
        .recordType(arincVhfNavaid.recordType())
        .customerAreaCode(arincVhfNavaid.customerAreaCode())
        .sectionCode(arincVhfNavaid.sectionCode())
        .subSectionCode(arincVhfNavaid.subSectionCode())
        .airportIdentifier(arincVhfNavaid.airportIdentifier())
        .airportIcaoRegion(arincVhfNavaid.airportIcaoRegion())
        .vhfIdentifier(arincVhfNavaid.vhfIdentifier())
        .vhfIcaoRegion(arincVhfNavaid.vhfIcaoRegion())
        .continuationRecordNumber(arincVhfNavaid.continuationRecordNumber())
        .vhfFrequency(arincVhfNavaid.vhfFrequency())
        .navaidClass(arincVhfNavaid.navaidClass())
        .latitude(arincVhfNavaid.latitude())
        .longitude(arincVhfNavaid.longitude())
        .dmeIdentifier(arincVhfNavaid.dmeIdentifier())
        .dmeLatitude(arincVhfNavaid.dmeLatitude())
        .dmeLongitude(arincVhfNavaid.dmeLongitude())
        .stationDeclination(arincVhfNavaid.stationDeclination())
        .dmeElevation(arincVhfNavaid.dmeElevation())
        .figureOfMerit(arincVhfNavaid.figureOfMerit())
        .ilsDmeBias(arincVhfNavaid.ilsDmeBias())
        .frequencyProtectionDistance(arincVhfNavaid.frequencyProtectionDistance())
        .datumCode(arincVhfNavaid.datumCode())
        .vhfNavaidName(arincVhfNavaid.vhfNavaidName())
        .fileRecordNumber(arincVhfNavaid.fileRecordNumber())
        .lastUpdateCycle(arincVhfNavaid.lastUpdateCycle())
        .build();
  }
}
