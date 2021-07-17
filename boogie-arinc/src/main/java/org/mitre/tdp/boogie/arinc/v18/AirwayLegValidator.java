package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Codified expectations on the field-level content of {@link ArincRecord}s which can be converted to {@link ArincAirwayLeg}s.
 */
public final class AirwayLegValidator implements Predicate<ArincRecord> {

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && arincRecord.containsParsedField("recordType")
        && arincRecord.containsParsedField("customerAreaCode")
        && arincRecord.containsParsedField("routeIdentifier")
        && arincRecord.containsParsedField("sequenceNumber")
        && arincRecord.containsParsedField("fixIdentifier")
        && arincRecord.containsParsedField("fixIcaoRegion")
        && arincRecord.containsParsedField("fixSectionCode")
        && arincRecord.containsParsedField("fileRecordNumber")
        && arincRecord.containsParsedField("lastUpdateCycle");
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.E::equals).isPresent() && subSectionCode.filter("R"::equals).isPresent();
  }
}
