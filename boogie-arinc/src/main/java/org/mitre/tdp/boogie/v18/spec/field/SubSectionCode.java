package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.FieldSpec;

public enum SubSectionCode implements FieldSpec<SubSectionCode> {
  SPEC;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.5";
  }

  @Override
  public SubSectionCode parse(String fieldString) {
    return SubSectionCode.valueOf(fieldString);
  }
}
