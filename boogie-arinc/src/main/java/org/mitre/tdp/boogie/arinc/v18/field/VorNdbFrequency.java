package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * The “VOR/NDB Frequency” field specifies the frequency of the NAVAID identified in the “VOR/NDB Identifier” field of the record.
 */
public final class VorNdbFrequency implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.34";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(ArincStrings::parseDoubleWithTenths);
  }
}
