package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseContinuationNumber;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * When it is not possible to store all the information needed on a record within the 132 columns of the record itself, the
 * so-called Primary Record; one or more continuation records may be used.
 * <br>
 * e.g. [0-9][A-Z]
 */
public final class ContinuationRecordNumber implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.16";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return apply(fieldValue, 0, fieldValue.length());
  }

  @Override
  public Optional<String> apply(String source, int startOffset, int endOffset) {
    return parseContinuationNumber(source, startOffset, endOffset);
  }
}
