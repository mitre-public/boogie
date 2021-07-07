package org.mitre.tdp.boogie.arinc.v18.spec;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ARINC spec isn't extremely specific about the required content of records (barring some of the procedure leg types) - this
 * class serves as a proxy for us (consumers of navigation information) to put expectations on what the content is of the records
 * we want to return and use in standard processing.
 */
public final class AirportValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(AirportValidator.class);

  @Override
  public boolean test(ArincRecord arincRecord) {
    LOG.debug("Beginning Airport record validation.");

    return isCorrectSectionSubSection(arincRecord)
        && arincRecord.containsParsedField("latitude")
        && arincRecord.containsParsedField("longitude")
        && arincRecord.containsParsedField("airportIdentifier")
        && arincRecord.containsParsedField("airportIcaoRegion")
        && arincRecord.containsParsedField("fileRecordNumber")
        && arincRecord.containsParsedField("lastUpdateCycle");
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("A"::equals).isPresent();
  }
}
