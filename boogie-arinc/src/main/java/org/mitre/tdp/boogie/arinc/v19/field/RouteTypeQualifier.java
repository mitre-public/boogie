package org.mitre.tdp.boogie.arinc.v19.field;

import static com.google.common.collect.Sets.newHashSet;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import java.util.Set;

import org.mitre.tdp.boogie.arinc.v18.field.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.arinc.v18.field.FreeFormString;

import com.google.common.collect.Sets;

public final class RouteTypeQualifier implements FreeFormString, FilterTrimEmptyInput<String> {

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
    checkSpec(this, rawField, allowedCodes.contains(rawField));
    return rawField;
  }

  /**
   * The collection of allowed route type qualifier codes.
   */
  private static final Set<String> allowedCodes = Sets.union(
      // V18 codes
      newHashSet("A", "B", "E", "C", "S", "D", "J", "L", "N", "P", "R", "T", "U", "V", "W"),
      // V19a codes
      newHashSet("H", "F", "I")
  );
}
