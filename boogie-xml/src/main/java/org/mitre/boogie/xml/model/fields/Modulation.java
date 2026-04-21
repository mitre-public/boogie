package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Modulation {
  AM_FREQ,
  FM_FREQ;
  public static final Set<String> VALID = Arrays.stream(Modulation.values()).map(Modulation::name).collect(Collectors.toUnmodifiableSet());
}
