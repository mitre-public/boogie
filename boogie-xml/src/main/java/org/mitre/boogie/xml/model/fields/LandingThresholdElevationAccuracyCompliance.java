package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum LandingThresholdElevationAccuracyCompliance {
  COMPLIANT,
  NOT_COMPLIANT,
  NOT_EVALUATED;
  public static final Set<String> VALID = Arrays.stream(LandingThresholdElevationAccuracyCompliance.values()).map(LandingThresholdElevationAccuracyCompliance::name).collect(Collectors.toUnmodifiableSet());
}
