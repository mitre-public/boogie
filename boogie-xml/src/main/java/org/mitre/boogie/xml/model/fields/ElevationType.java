package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ElevationType {
  LANDING_THRESHOLD,
  PORT,
  RUNWAY_END,
  TOUCHDOWN_ZONE;
  public static final Set<String> VALID = Arrays.stream(ElevationType.values()).map(ElevationType::name).collect(Collectors.toUnmodifiableSet());
}
