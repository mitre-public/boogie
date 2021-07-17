package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Spec for records which have explicit blank space in them.
 */
public final class BlankSpec implements FieldSpec<Void> {

  private final int fieldLength;

  public BlankSpec(int fieldLength) {
    this.fieldLength = fieldLength;
  }

  @Override
  public int fieldLength() {
    return fieldLength;
  }

  @Override
  public String fieldCode() {
    throw new UnsupportedOperationException("Cannot get field code for empty field.");
  }

  @Override
  public Void parseValue(String fieldValue) {
    throw new UnsupportedOperationException("Unable to apply parsing logic to blank record portion.");
  }

  public static BlankSpec ofLength(int fieldLength) {
    return new BlankSpec(fieldLength);
  }
}
