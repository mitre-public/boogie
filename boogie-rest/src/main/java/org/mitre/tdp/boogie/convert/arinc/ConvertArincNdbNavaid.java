package org.mitre.tdp.boogie.convert.arinc;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.arinc.ArincNdbNavaid;
import org.mitre.tdp.boogie.contract.arinc.ImmutableArincNdbNavaid;

public final class ConvertArincNdbNavaid implements Function<org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid, ArincNdbNavaid> {
  public static final ConvertArincNdbNavaid INSTANCE = new ConvertArincNdbNavaid();

  private ConvertArincNdbNavaid() {

  }

  @Override
  public ArincNdbNavaid apply(org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid arincNdbNavaid) {
    requireNonNull(arincNdbNavaid);
    return ImmutableArincNdbNavaid.builder()
        .recordType(arincNdbNavaid.recordType())
        .customerAreaCode(arincNdbNavaid.customerAreaCode())
        .sectionCode(arincNdbNavaid.sectionCode())
        .subSectionCode(arincNdbNavaid.subSectionCode())
        .airportIdentifier(arincNdbNavaid.airportIdentifier())
        .airportIcaoRegion(arincNdbNavaid.airportIcaoRegion())
        .ndbIdentifier(arincNdbNavaid.ndbIdentifier())
        .ndbIcaoRegion(arincNdbNavaid.ndbIcaoRegion())
        .continuationRecordNumber(arincNdbNavaid.continuationRecordNumber())
        .ndbFrequency(arincNdbNavaid.ndbFrequency())
        .navaidClass(arincNdbNavaid.navaidClass())
        .latitude(arincNdbNavaid.latitude())
        .longitude(arincNdbNavaid.longitude())
        .magneticVariation(arincNdbNavaid.magneticVariation())
        .datumCode(arincNdbNavaid.datumCode())
        .ndbNavaidName(arincNdbNavaid.ndbNavaidName())
        .fileRecordNumber(arincNdbNavaid.fileRecordNumber())
        .lastUpdateCycle(arincNdbNavaid.lastUpdateCycle())
        .build();
  }
}
