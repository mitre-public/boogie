package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NdbNavaidCoverage {
  HIGH_POWER_NDB,
  LOW_POWER_NDB,
  LOCATOR,
  NDB;
  public static final Set<String> VALID = Arrays.stream(NdbNavaidCoverage.values()).map(NdbNavaidCoverage::name).collect(Collectors.toUnmodifiableSet());
}
