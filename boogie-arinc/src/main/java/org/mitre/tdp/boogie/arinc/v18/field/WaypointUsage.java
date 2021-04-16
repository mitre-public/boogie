package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.isBlank;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The waypoint usage field is employed to indicate the structure in which the waypoint is utilized.
 */
public class WaypointUsage implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.82";
  }

  @Override
  public String parseValue(String fieldValue) {
    checkSpec(this, fieldValue, fieldValue.startsWith("R") || isBlank.test(fieldValue.substring(0, 1)));
    checkSpec(this, fieldValue, allowedColumn2().contains(fieldValue.substring(1, 2)));
    return fieldValue;
  }

  public List<String> allowedColumn2() {
    return Arrays.asList("B", "H", "L", " ");
  }
}
