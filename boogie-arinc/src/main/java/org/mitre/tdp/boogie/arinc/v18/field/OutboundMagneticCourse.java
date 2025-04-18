package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * “Outbound Magnetic Course” is the published outbound magnetic course from the waypoint identified in the record’s “Fix Ident”
 * field. In addition, this field is used for Course/Heading/Radials on SID/STAR Approach Records through requirements of the
 * Path Terminator and coding rules contained in Attachment 5 of this specification.
 * <br>
 * Values from official government sources will be used when available. The field contains magnetic information expressed in
 * degrees and tenths of a degree, with the decimal point suppressed.
 * <br>
 * e.g. 2760, 0231, 194T
 * <br>
 * Handling of field contents is similar to {@link InboundMagneticCourse}.
 */
public final class OutboundMagneticCourse implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.26";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        // explicit drop true course values... we could add handling for these later
        .filter(s -> !s.endsWith("T"))
        .flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
