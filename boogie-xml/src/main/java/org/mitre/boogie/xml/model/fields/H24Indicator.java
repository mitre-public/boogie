package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum H24Indicator {
  CONTINUOUS,
  NOT_CONTINUOUS,
  UNKNOWN;
  public static final Set<String> VALID = Arrays.stream(H24Indicator.values()).map(H24Indicator::name).collect(Collectors.toUnmodifiableSet());
}
