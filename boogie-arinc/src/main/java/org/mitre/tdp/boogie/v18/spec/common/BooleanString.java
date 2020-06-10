package org.mitre.tdp.boogie.v18.spec.common;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * Interface for fields representing boolean return values (either Y/N, T/F, true/false).
 */
public interface BooleanString extends FieldSpec<Boolean>, FilterTrimEmptyInput<Boolean> {

  @Override
  default Boolean parseValue(String fieldString) {
    return trueValues().stream().anyMatch(value -> value.equalsIgnoreCase(fieldString));
  }

  default List<String> trueValues() {
    return Arrays.asList("Y", "True", "yes");
  }
}
