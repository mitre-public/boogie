package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseLongitude;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Longitude field contains the longitude of the geographic position of the navigational feature identified in the record.
 */
public final class Longitude implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 10;
  }

  @Override
  public String fieldCode() {
    return "5.37";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return apply(fieldValue, 0, fieldValue.length());
  }

  @Override
  public Optional<Double> apply(String source, int startOffset, int endOffset) {
    return parseLongitude(source, startOffset, endOffset);
  }
}
