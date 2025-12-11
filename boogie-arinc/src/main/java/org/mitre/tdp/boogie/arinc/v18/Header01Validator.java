package org.mitre.tdp.boogie.arinc.v18;

import static org.mitre.tdp.boogie.arinc.ValidationHelper.containsParsedField;
import static org.mitre.tdp.boogie.arinc.v18.field.HeaderIdent.HDR;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Header01Validator implements Predicate<ArincRecord> {
  private static final Logger LOG = LoggerFactory.getLogger(Header01Validator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public Header01Validator() {
    this((arincRecord, field) ->
        LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public Header01Validator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return noSectionSubsection(arincRecord)
        && containsParsedField(arincRecord, "headerIdent", missingFieldConsumer)
        && containsParsedField(arincRecord, "headerNumber", missingFieldConsumer)
        && arincRecord.requiredField("headerIdent").equals(HDR)
        && arincRecord.requiredField("headerNumber").equals(1);
  }

  private boolean noSectionSubsection(ArincRecord arincRecord) {
    return !arincRecord.containsParsedField("sectionCode") && !arincRecord.containsParsedField("subSectionCode");
  }
}
