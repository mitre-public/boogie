package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public final class VhfNavaidValidator implements Predicate<ArincRecord> {

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && arincRecord.containsParsedField("recordType")
        && arincRecord.containsParsedField("vhfIdentifier")
        && arincRecord.containsParsedField("vhfIcaoRegion")
        && arincRecord.containsParsedField("latitude")
        && arincRecord.containsParsedField("longitude")
        && arincRecord.containsParsedField("fileRecordNumber")
        && arincRecord.containsParsedField("lastUpdateCycle");
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    return sectionCode.filter(SectionCode.D::equals).isPresent();
  }
}
