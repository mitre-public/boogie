package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RunwaySuffix {
  WATER_SEALANE_OR_WATERWAY,
  GLIDER_RUNWAY,
  ULTRALIGHT_RUNWAY;
  public static final Set<String> VALID = Arrays.stream(RunwaySuffix.values()).map(RunwaySuffix::name).collect(Collectors.toUnmodifiableSet());
}
