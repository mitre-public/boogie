package org.mitre.tdp.boogie.v18.spec.field;

import static com.google.common.collect.Sets.newHashSet;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import java.util.HashSet;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Route Type” field defines the type of Enroute Airway, Preferred Route, Airport and Heliport SID/STAR/Approach Routes of which
 * the record is an element. For Airport and Heliport Approach Routes, “Route Type” includes a “primary route type,” and up to two
 * “route type qualifiers.”
 */
public class RouteTypeQualifier implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a";
  }

  @Override
  public String parseValue(String rawField) {
    checkSpec(this, rawField, allowedCodes().contains(rawField));
    return rawField;
  }

  /**
   * The collection of allowed route type qualifier codes.
   */
  public static HashSet<String> allowedCodes() {
    return newHashSet("A", "B", "E", "C", "S", "D", "J", "L", "N", "P", "R", "T", "U", "V", "W");
  }
}
