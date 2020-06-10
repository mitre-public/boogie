package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Datum Code” field defines the Local Horizontal Reference Datum to which a geographical position, expressed in latitude and longitude, is associated.
 */
public class DatumCode implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.197";
  }
}
