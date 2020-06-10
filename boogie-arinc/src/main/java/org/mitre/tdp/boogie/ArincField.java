package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An {@link ArincField} decorates an named instance of a {@link FieldSpec} within a record.
 *
 * These fields are unique identified by name within a given record type and may be referenced
 * as such from the top level record.
 */
public interface ArincField<T> {

  /**
   * The unique name of the field within the record.
   */
  String fieldName();

  /**
   * Returns the associated {@link FieldSpec} of the named arinc field.
   */
  FieldSpec<T> fieldSpec();

  /**
   * Generates a new instance of an ARINC field with the provided field spec.
   */
  static <T> Impl<T> newField(String fieldName, FieldSpec<T> fieldSpec) {
    checkArgument(!(fieldSpec instanceof Enum) || ((Enum) fieldSpec).name().equals("SPEC"), "Enum-based FieldSpecs must use reserved SPEC value for spec parsing.");
    return new Impl<>(fieldName, fieldSpec);
  }

  /**
   * Generates a new instance of an ARINC field with the provided {@link FieldSpec#fieldName()}.
   */
  static <T> Impl<T> newField(FieldSpec<T> fieldSpec) {
    return newField(fieldSpec.fieldName(), fieldSpec);
  }

  /**
   * Concrete implementation of {@link ArincField} interface.
   */
  class Impl<T> implements ArincField<T> {

    private String fieldName;
    private FieldSpec<T> fieldSpec;

    public Impl(String fieldName, FieldSpec<T> fieldSpec) {
      this.fieldName = fieldName;
      this.fieldSpec = fieldSpec;
    }

    @Override
    public String fieldName() {
      return fieldName;
    }

    @Override
    public FieldSpec<T> fieldSpec() {
      return fieldSpec;
    }
  }
}
