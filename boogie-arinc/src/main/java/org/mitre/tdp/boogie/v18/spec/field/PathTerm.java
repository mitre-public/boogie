package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * The Path and Termination defines the path geometry for a single record of an ATC terminal procedure.
 */
public class PathTerm implements FieldSpec<org.mitre.tdp.boogie.PathTerm> {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.21";
  }

  @Override
  public org.mitre.tdp.boogie.PathTerm parse(String fieldString) {
    return toEnumValue(fieldString, org.mitre.tdp.boogie.PathTerm.class, getClass());
  }
}
