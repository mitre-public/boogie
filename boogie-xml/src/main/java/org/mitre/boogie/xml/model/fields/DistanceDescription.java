package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum DistanceDescription {
  OUT_TO_SPECIFIED_DISTANCE,
  APPLIES_BEYOND_DISTANCE,
  NO_RESTRICTION;
  public static final Set<String> VALID = Arrays.stream(DistanceDescription.values()).map(DistanceDescription::name).collect(Collectors.toUnmodifiableSet());
}
