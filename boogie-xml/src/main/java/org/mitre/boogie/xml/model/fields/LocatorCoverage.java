package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum LocatorCoverage {
  HIGH_POWER_NDB,
  NDB,
  LOW_POWER_NDB,
  LOCATOR;
  public static final Set<String> VALID = Arrays.stream(LocatorCoverage.values()).map(LocatorCoverage::name).collect(Collectors.toUnmodifiableSet());
}
