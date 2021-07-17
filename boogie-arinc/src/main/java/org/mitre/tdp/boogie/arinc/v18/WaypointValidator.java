package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public final class WaypointValidator implements Predicate<ArincRecord> {

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && arincRecord.containsParsedField("recordType")
        && arincRecord.containsParsedField("waypointIdentifier")
        && arincRecord.containsParsedField("waypointIcaoRegion")
        && arincRecord.containsParsedField("latitude")
        && arincRecord.containsParsedField("longitude")
        && arincRecord.containsParsedField("fileRecordNumber")
        && arincRecord.containsParsedField("lastUpdateCycle");
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> enrouteSubSectionCode = arincRecord.optionalField("enrouteSubSectionCode");
    Optional<String> terminalSubSectionCode = arincRecord.optionalField("terminalSubSectionCode");

    return (sectionCode.filter(SectionCode.P::equals).isPresent() && terminalSubSectionCode.filter("C"::equals).isPresent())
        || (sectionCode.filter(SectionCode.E::equals).isPresent() && enrouteSubSectionCode.filter("A"::equals).isPresent());
  }
}
