package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum VhfNavaidCoverage {
  TERMINAL,
  LOW,
  HIGH,
  UNDEFINED,
  ILS_TACAN;
  public static final Set<String> VALID = Arrays.stream(VhfNavaidCoverage.values()).map(VhfNavaidCoverage::name).collect(Collectors.toUnmodifiableSet());
}
