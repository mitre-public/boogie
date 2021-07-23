package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Waypoint Type” field defines both the “type” and function of IFR waypoints and also define a waypoint as being VFR.
 */
public final class WaypointType implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.42";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(s -> s.length() == 3)
        .map(s -> ""
            .concat(inSetOrBlank(s.substring(0, 1), allowedColumn1))
            .concat(inSetOrBlank(s.substring(1, 2), allowedColumn2))
            .concat(inSetOrBlank(s.substring(2, 3), allowedColumn3))
        );
  }

  private String inSetOrBlank(String s, HashSet<String> set) {
    return set.contains(s) ? s : " ";
  }

  private static final HashSet<String> allowedColumn1 = newHashSet("C", "I", "N", "R", "U", "V", "W", "A", "M", "O");

  private static final HashSet<String> allowedColumn2 = newHashSet("A", "B", "C", "D", "E", "F", "I", "K", "L", "M", "N", "O", "P", "S", "U", "V", "W");

  private static final HashSet<String> allowedColumn3 = newHashSet("D", "E", "F", "Z");
}
