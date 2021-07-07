package org.mitre.tdp.boogie.arinc.v18.spec;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public final class LocalizerGlideSlopeValidator implements Predicate<ArincRecord> {

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && (!arincRecord.containsParsedField("supportingFacilityIdentifier") || containsCompleteSupportingFacilityInformation(arincRecord))
        && arincRecord.containsParsedField("recordType")
        && arincRecord.containsParsedField("airportIdentifier")
        && arincRecord.containsParsedField("airportIcaoRegion")
        && arincRecord.containsParsedField("localizerIdentifier")
        && arincRecord.containsParsedField("runwayIdentifier")
        && arincRecord.containsParsedField("fileRecordNumber")
        && arincRecord.containsParsedField("lastUpdateCycle");
  }

  boolean containsCompleteSupportingFacilityInformation(ArincRecord arincRecord) {
    return arincRecord.containsParsedField("supportingFacilityIdentifier")
        && arincRecord.containsParsedField("supportingFacilityIcaoRegion")
        && arincRecord.containsParsedField("supportingFacilitySectionCode");
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("I"::equals).isPresent();
  }
}
