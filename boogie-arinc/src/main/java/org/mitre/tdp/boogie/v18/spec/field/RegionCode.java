package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Region Code” permits the categorization of waypoints and holding patterns as either enroute or terminal area waypoints.
 * In the latter case the terminal area airport is identified in the field.
 */
public class RegionCode implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.41";
  }
}
