package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableSet;

/**
 * The waypoint usage field is employed to indicate the structure in which the waypoint is utilized.
 */
public final class WaypointUsage implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.82";
  }

  private static final ImmutableSet<String> allowedColumn2 = ImmutableSet.of("B", "H", "L", " ");

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(s -> s.length() == 2)
        .filter(s -> s.startsWith("R") || s.startsWith(" "))
        .filter(s -> allowedColumn2.contains(s.substring(1)));
  }
}
