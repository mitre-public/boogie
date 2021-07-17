package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public final class NdbNavaidValidator implements Predicate<ArincRecord> {

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && arincRecord.containsParsedField("recordType")
        && arincRecord.containsParsedField("ndbIdentifier")
        && arincRecord.containsParsedField("ndbIcaoRegion")
        && arincRecord.containsParsedField("latitude")
        && arincRecord.containsParsedField("longitude")
        && arincRecord.containsParsedField("fileRecordNumber")
        && arincRecord.containsParsedField("lastUpdateCycle");
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return (sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("N"::equals).isPresent())
        || (sectionCode.filter(SectionCode.D::equals).isPresent() && subSectionCode.filter("B"::equals).isPresent());
  }
}
