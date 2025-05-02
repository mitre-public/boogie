package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Cycle Date” field identifies the calendar period in which the record was added to the file or last revised. A change in
 * any ARINC 424 field, except Dynamic Magnetic V ariation, Frequency Protection, Continuation Record Number and File Record Number,
 * requires a cycle date change. The cycle date will not change if there is no change in the data.
 */
public final class Cycle implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.31";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .filter(AiracCycle::isValidCycle);
  }
}
