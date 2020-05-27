package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * Spec for records which have explicit blank space in them.
 */
public class BlankSpec implements FieldSpec<Void> {

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
  public Void parse(String field) {
    throw new UnsupportedOperationException("Unable to parse a string empty field.");
  }

  public static BlankSpec ofLength(int fieldLength) {
    return new BlankSpec(fieldLength);
  }
}
