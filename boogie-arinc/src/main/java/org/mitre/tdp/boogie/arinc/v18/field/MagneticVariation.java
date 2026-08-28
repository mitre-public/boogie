package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseEastWestDouble;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Magnetic Variation” field specifies the angular difference between True North and Magnetic North at the location defined in the record.
 * “Dynamic Magnetic Variation” is a computer model derived value and takes location and date into consideration. For the “Station Declination”
 * used in some record types, refer to Section 5.66.
 * <br>
 * e.g. E0140, E0000, T0000
 * <br>
 * As in {@link InboundMagneticCourse} this class filters out variations listed as true north. Use {@link org.mitre.tdp.boogie.Declinations} instead for your work.
 */
public final class MagneticVariation implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.39";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return apply(fieldValue, 0, fieldValue.length());
  }

  @Override
  public Optional<Double> apply(String source, int startOffset, int endOffset) {
    return parseEastWestDouble(source, startOffset, endOffset, 1);
  }
}
