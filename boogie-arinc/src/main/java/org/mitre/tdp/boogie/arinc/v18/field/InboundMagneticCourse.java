package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * “Inbound Magnetic Course” is the published inbound magnetic course to the waypoint in the “Fix Ident” field of the
 * records in which it is employed.
 */
public final  class InboundMagneticCourse implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.28";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, !isTrueCourse(fieldValue));
    return ArincStrings.parseDoubleWithTenths(fieldValue);
  }

  /**
   * Returns true when the inbound course is wrt true north.
   */
  public boolean isTrueCourse(String fieldString) {
    return fieldString.endsWith("T");
  }
}
