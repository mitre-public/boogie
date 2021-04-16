package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Input spec for classes which should pre-filter inputs to their parseValue methods based on whether the
 * trimmed input string is empty.
 */
public interface FilterTrimEmptyInput<T> extends FieldSpec<T> {

  @Override
  default boolean filterInput(String fieldValue) {
    return fieldValue.trim().isEmpty();
  }
}
