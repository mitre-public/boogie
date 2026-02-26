package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum FrequencyUnits {
  LOW_FREQ,
  MEDIUM_FREQ,
  HIGH_FREQ,
  VHF_NON_STANDARD,
  UHF,
  DIGITAL_SERVICE;
  public static final Set<String> VALID = Arrays.stream(FrequencyUnits.values()).map(FrequencyUnits::name).collect(Collectors.toUnmodifiableSet());
}
