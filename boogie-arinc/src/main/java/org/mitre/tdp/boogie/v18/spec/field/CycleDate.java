package org.mitre.tdp.boogie.v18.spec.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import java.time.Instant;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.AiracCycle;

/**
 * The “Cycle Date” field identifies the calendar period in which the record was added to the file or last revised. A change in
 * any ARINC 424 field, except Dynamic Magnetic V ariation, Frequency Protection, Continuation Record Number and File Record Number,
 * requires a cycle date change. The cycle date will not change if there is no change in the data.
 */
public class CycleDate implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.31";
  }

  @Override
  public String parse(String fieldString) {
    checkSpec(this, fieldString, isNumeric(fieldString));
    return fieldString;
  }

  public Instant asStartDate(String fieldString) {
    return new AiracCycle(fieldString).startDate();
  }
}
