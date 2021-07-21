package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * “Inbound Magnetic Course” is the published inbound magnetic course to the waypoint in the “Fix Ident” field of the records in
 * which it is employed.
 * <br>
 * Values from official government sources will be used when available. The field contains magnetic bearing in degrees and tenths
 * of a degree, with the decimal point suppressed. For routes charted with true courses, the last character of this field will
 * contain a “T” in place of tenths of a degree.
 * <br>
 * For now this parser explicitly drops "True Courses" from the field spec - as they are relatively uncommon and dealing with the
 * true course component downstream is annoying.
 */
public final class InboundMagneticCourse implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.28";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        // explicit drop true course values... we could add handling for these later
        .filter(s -> !s.endsWith("T"))
        .filter(ValidArincNumeric.INSTANCE)
        .map(ArincStrings::parseDoubleWithTenths);
  }
}
