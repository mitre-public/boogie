package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NavaidWeatherInfo {
  AUTOMATED;
  public static final Set<String> VALID = Arrays.stream(NavaidWeatherInfo.values()).map(NavaidWeatherInfo::name).collect(Collectors.toUnmodifiableSet());
}
