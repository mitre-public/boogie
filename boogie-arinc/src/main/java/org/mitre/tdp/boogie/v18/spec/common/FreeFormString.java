package org.mitre.tdp.boogie.v18.spec.common;

import org.mitre.tdp.boogie.FieldSpec;

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
