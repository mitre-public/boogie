package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Interface for free-form string fields.
 */
public interface FreeFormString extends FieldSpec<String> {

  /**
   * Returns whether the field value should be {@link String#trim()}ed prior to return.
   */
  default boolean shouldTrim() {
    return true;
  }

  @Override
  default String parseValue(String fieldValue) {
    return shouldTrim() ? fieldValue.trim() : fieldValue;
  }
}
