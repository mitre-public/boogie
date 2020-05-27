package org.mitre.tdp.boogie.v18.spec.common;

import org.mitre.tdp.boogie.FieldSpec;

public interface FreeFormString extends FieldSpec<String> {

  @Override
  default String parse(String fieldString) {
    return fieldString;
  }
}
