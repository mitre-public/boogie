package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * “Outbound Magnetic Course” is the published outbound magnetic course from the waypoint identified in the record’s
 * “Fix Ident” field. In addition, this field is used for Course/Heading/Radials on SID/STAR Approach Records through
 * requirements of the Path Terminator and coding rules contained in Attachment 5 of this specification.
 */
public class OutboundMagneticCourse implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.26";
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
