package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.ValidationHelper.containsParsedField;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VhfNavaidValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(VhfNavaidValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public VhfNavaidValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public VhfNavaidValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && checkIdentifier(arincRecord)
        && containsParsedField(arincRecord, "vhfIcaoRegion", missingFieldConsumer)
        && checkLatLon(arincRecord)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer);
  }

  boolean checkIdentifier(ArincRecord arincRecord){
    boolean vhfPresent = arincRecord.optionalField("vhfIdentifier").isPresent();
    boolean dmePresent = arincRecord.optionalField("dmeIdentifier").isPresent();

    if (!(vhfPresent || dmePresent)){
      missingFieldConsumer.accept(arincRecord, "Identifier");
    }

    return vhfPresent || dmePresent;
  }

  boolean checkLatLon(ArincRecord arincRecord) {
    boolean standardPresent = arincRecord.optionalField("latitude").isPresent() && arincRecord.optionalField("longitude").isPresent();
    boolean dmePresent = arincRecord.optionalField("dmeLatitude").isPresent() && arincRecord.optionalField("dmeLongitude").isPresent();

    if (!(standardPresent || dmePresent)) {
      missingFieldConsumer.accept(arincRecord, "Latitude/Longitude");
    }

    return standardPresent || dmePresent;
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    return sectionCode.filter(SectionCode.D::equals).isPresent();
  }
}
