package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RunwayUsageIndicator {
  LANDING_ONLY,
  TAKEOFF_ONLY,
  TAKEOFF_AND_LANDING;
  public static final Set<String> VALID = Arrays.stream(RunwayUsageIndicator.values()).map(RunwayUsageIndicator::name).collect(Collectors.toUnmodifiableSet());
}
