package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Name Format Indicator” field is used to describe the format of the “Waypoint Name/Description” field (5.43). This field
 * will be formatted according to the rules described in Chapter 7 of this Specification, Waypoint Naming Conventions.
 */
public final class NameFormat implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.196";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(s -> s.length() == 3)
        .map(s -> new String()
            .concat(inSetOrBlank(s.substring(0, 1), allowedColumn1))
            .concat(inSetOrBlank(s.substring(1, 2), allowedColumn2))
            .concat(inSetOrBlank(s.substring(2, 3), allowedColumn3))
        );
  }

  private String inSetOrBlank(String s, HashSet<String> set) {
    return set.contains(s) ? s : " ";
  }

  private static final HashSet<String> allowedColumn1 = newHashSet("A", "B", "D", "F", "H", "I", "L", "M", "N", "P", "Q", "R", "T", "U");

  private static final HashSet<String> allowedColumn2 = newHashSet("O", "M");

  private static final HashSet<String> allowedColumn3 = newHashSet();
}
