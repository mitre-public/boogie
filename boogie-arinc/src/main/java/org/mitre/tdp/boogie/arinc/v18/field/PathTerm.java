package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Path and Termination defines the path geometry for a single record of an ATC terminal procedure.
 */
public final class PathTerm implements FieldSpec<org.mitre.tdp.boogie.PathTerm>, FilterTrimEmptyInput<org.mitre.tdp.boogie.PathTerm> {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.21";
  }

  @Override
  public org.mitre.tdp.boogie.PathTerm parseValue(String fieldValue) {
    return toEnumValue(fieldValue, org.mitre.tdp.boogie.PathTerm.class, getClass());
  }
}
