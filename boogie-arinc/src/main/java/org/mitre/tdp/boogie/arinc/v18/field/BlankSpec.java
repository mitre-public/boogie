package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

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

  public static BlankSpec ofLength(int fieldLength) {
    return new BlankSpec(fieldLength);
  }

  @Override
  public Optional<Void> apply(String s) {
    throw new UnsupportedOperationException("Cannot apply blank spec for field parsing.");
  }
}
