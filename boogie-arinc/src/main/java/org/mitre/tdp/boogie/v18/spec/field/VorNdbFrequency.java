package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.apache.commons.lang3.StringUtils;
import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;

/**
 * The “VOR/NDB Frequency” field specifies the frequency of the NAVAID identified in the “VOR/NDB Identifier” field of the record.
 */
public class VorNdbFrequency implements FieldSpec<Float> {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.34";
  }

  @Override
  public Float parse(String fieldString) {
    checkSpec(this, fieldString, StringUtils.isNumeric(fieldString));
    return ArincStrings.parseFloatWithTenths(fieldString);
  }
}
