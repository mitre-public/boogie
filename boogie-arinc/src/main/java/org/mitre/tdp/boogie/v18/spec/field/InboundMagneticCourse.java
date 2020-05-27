package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;

/**
 * “Inbound Magnetic Course” is the published inbound magnetic course to the waypoint in the “Fix Ident” field of the
 * records in which it is employed.
 */
public class InboundMagneticCourse implements FieldSpec<Double> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.28";
  }

  @Override
  public Double parse(String fieldString) {
    checkSpec(this, fieldString, !isTrueCourse(fieldString));
    return ArincStrings.parseDoubleWithTenths(fieldString);
  }

  /**
   * Returns true when the inbound course is wrt true north.
   */
  public boolean isTrueCourse(String fieldString) {
    return fieldString.endsWith("T");
  }
}
