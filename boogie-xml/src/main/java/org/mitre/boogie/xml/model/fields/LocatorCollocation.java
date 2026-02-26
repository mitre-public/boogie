package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum LocatorCollocation {
  BFO_OPERATION,
  LOCATOR_MARKER_COLLOCATED,
  LOCATOR_MIDDLE_MARKER_NOT_COLLOCATED;
  public static final Set<String> VALID = Arrays.stream(LocatorCollocation.values()).map(LocatorCollocation::name).collect(Collectors.toUnmodifiableSet());
}
