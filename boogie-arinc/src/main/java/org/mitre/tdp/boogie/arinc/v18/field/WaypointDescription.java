package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Fixes are located at positions significant to navigation in the Enroute, Terminal Area and Approach Procedure path definitions.
 * The “Waypoint Description Code” field enables that significance or function of a fix at a specific location in a route to be
 * identified. The field provides information on the type of fix. As a single fix can be used in different route structures and
 * multiple times within a given structure, the field provides the function for each occurrence of a fix.
 */
public final class WaypointDescription implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.17";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(s -> s.length() == 4)
        .map(s -> ""
            .concat(inSetOrBlank(s.substring(0, 1), allowedColumn1))
            .concat(inSetOrBlank(s.substring(1, 2), allowedColumn2))
            .concat(inSetOrBlank(s.substring(2, 3), allowedColumn3))
            .concat(inSetOrBlank(s.substring(3, 4), allowedColumn4))
        );
  }

  private String inSetOrBlank(String s, HashSet<String> set) {
    return set.contains(s) ? s : " ";
  }

  private static final HashSet<String> allowedColumn1 = newHashSet("A", "E", "F", "G", "H", "N", "P", "R", "T", "V");

  private static final HashSet<String> allowedColumn2 = newHashSet("B", "E", "U", "Y");

  private static final HashSet<String> allowedColumn3 = newHashSet("A", "B", "C", "G", "M", "P", "R", "S");

  private static final HashSet<String> allowedColumn4 = newHashSet("A", "B", "C", "D", "E", "F", "G", "H", "I", "M", "N");
}
