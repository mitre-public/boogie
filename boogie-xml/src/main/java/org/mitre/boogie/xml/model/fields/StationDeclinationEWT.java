package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum StationDeclinationEWT {
  EAST,
  WEST,
  TRUE,
  GRID;
  public static final Set<String> VALID = Arrays.stream(StationDeclinationEWT.values()).map(StationDeclinationEWT::name).collect(Collectors.toUnmodifiableSet());
}
