package org.mitre.tdp.boogie.v18.spec.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;

/**
 * The magnetic bearing of the runway identified in the “runway identifier” field of the record is specified in the “Runway Magnetic Bearing” field.
 */
public class RunwayMagneticBearing implements FieldSpec<Float> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.58";
  }

  @Override
  public Float parse(String fieldString) {
    checkSpec(this, fieldString, !fieldString.endsWith("T"));
    checkSpec(this, fieldString, isNumeric(fieldString));
    return ArincStrings.parseFloatWithTenths(fieldString);
  }
}
